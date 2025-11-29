package eco.kosova.infrastructure.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * Manager për leximin dhe shkruar në JSON files.
 * 
 * Thread-safe implementation me ReadWriteLock.
 * Përdor Gson për serialization/deserialization.
 */
public class JsonDataManager {
    
    private static final Logger logger = Logger.getLogger(JsonDataManager.class.getName());
    
    private final Gson gson;
    private final String dataDirectory;
    private final ReadWriteLock lock;
    
    /**
     * Constructor me directory default
     */
    public JsonDataManager() {
        this("backend/src/main/resources/data");
    }
    
    /**
     * Constructor me custom directory
     */
    public JsonDataManager(String dataDirectory) {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();
        
        this.dataDirectory = dataDirectory;
        this.lock = new ReentrantReadWriteLock();
        
        // Krijo directory nëse nuk ekziston
        createDataDirectoryIfNotExists();
    }
    
    /**
     * Lexon një listë objektesh nga JSON file
     */
    public <T> List<T> readList(String filename, Type typeToken) {
        lock.readLock().lock();
        try {
            Path filePath = Paths.get(dataDirectory, filename);
            
            if (!Files.exists(filePath)) {
                logger.warning(String.format("File not found: %s. Returning empty list.", filename));
                return new ArrayList<>();
            }
            
            try (Reader reader = new FileReader(filePath.toFile())) {
                List<T> data = gson.fromJson(reader, typeToken);
                return data != null ? data : new ArrayList<>();
            } catch (IOException e) {
                logger.severe(String.format("Error reading file %s: %s", filename, e.getMessage()));
                return new ArrayList<>();
            }
            
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Shkruan një listë objektesh në JSON file
     */
    public <T> void writeList(String filename, List<T> data) {
        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(dataDirectory, filename);
            
            // Krijo parent directory nëse nuk ekziston
            Files.createDirectories(filePath.getParent());
            
            try (Writer writer = new FileWriter(filePath.toFile())) {
                gson.toJson(data, writer);
                logger.fine(String.format("Successfully wrote %d items to %s", data.size(), filename));
            } catch (IOException e) {
                logger.severe(String.format("Error writing file %s: %s", filename, e.getMessage()));
                throw new RuntimeException("Failed to write data to file: " + filename, e);
            }
            
        } catch (IOException e) {
            logger.severe(String.format("Error creating directory: %s", e.getMessage()));
            throw new RuntimeException("Failed to create data directory", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Lexon një objekt të vetëm nga JSON file
     */
    public <T> T readObject(String filename, Class<T> clazz) {
        lock.readLock().lock();
        try {
            Path filePath = Paths.get(dataDirectory, filename);
            
            if (!Files.exists(filePath)) {
                logger.warning(String.format("File not found: %s", filename));
                return null;
            }
            
            try (Reader reader = new FileReader(filePath.toFile())) {
                return gson.fromJson(reader, clazz);
            } catch (IOException e) {
                logger.severe(String.format("Error reading file %s: %s", filename, e.getMessage()));
                return null;
            }
            
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Shkruan një objekt të vetëm në JSON file
     */
    public <T> void writeObject(String filename, T data) {
        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(dataDirectory, filename);
            
            Files.createDirectories(filePath.getParent());
            
            try (Writer writer = new FileWriter(filePath.toFile())) {
                gson.toJson(data, writer);
                logger.fine(String.format("Successfully wrote object to %s", filename));
            } catch (IOException e) {
                logger.severe(String.format("Error writing file %s: %s", filename, e.getMessage()));
                throw new RuntimeException("Failed to write data to file: " + filename, e);
            }
            
        } catch (IOException e) {
            logger.severe(String.format("Error creating directory: %s", e.getMessage()));
            throw new RuntimeException("Failed to create data directory", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Kontrollon nëse një file ekziston
     */
    public boolean fileExists(String filename) {
        Path filePath = Paths.get(dataDirectory, filename);
        return Files.exists(filePath);
    }
    
    /**
     * Fshin një file
     */
    public boolean deleteFile(String filename) {
        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(dataDirectory, filename);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info(String.format("Deleted file: %s", filename));
                return true;
            }
            
            return false;
            
        } catch (IOException e) {
            logger.severe(String.format("Error deleting file %s: %s", filename, e.getMessage()));
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Krijo data directory nëse nuk ekziston
     */
    private void createDataDirectoryIfNotExists() {
        try {
            Path path = Paths.get(dataDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info(String.format("Created data directory: %s", dataDirectory));
            }
        } catch (IOException e) {
            logger.severe(String.format("Failed to create data directory: %s", e.getMessage()));
            throw new RuntimeException("Failed to initialize data directory", e);
        }
    }
    
    /**
     * Merr path-in e plotë të data directory
     */
    public String getDataDirectory() {
        return dataDirectory;
    }
    
    /**
     * Merr Gson instance (për custom serialization nëse nevojitet)
     */
    public Gson getGson() {
        return gson;
    }
}
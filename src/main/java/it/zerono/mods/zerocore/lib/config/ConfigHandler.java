package it.zerono.mods.zerocore.lib.config;

import it.zerono.mods.zerocore.util.CodeHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public abstract class ConfigHandler implements IConfigListener {
      private final String _languageKeyPrefix;
      private final String _configFileName;
      private final String _configDirectoryName;
      private final List _categories;
      private final List _listeners;
      private final Configuration _configuration;

      public ConfigHandler(String fileName) {
            this(fileName, (String)null, (String)null);
      }

      public ConfigHandler(String fileName, String directoryName) {
            this(fileName, directoryName, (String)null);
      }

      public ConfigHandler(String fileName, String directoryName, String languageKeyPrefix) {
            if (null != languageKeyPrefix && !languageKeyPrefix.isEmpty()) {
                  if (!languageKeyPrefix.endsWith(".")) {
                        this._languageKeyPrefix = languageKeyPrefix + ".";
                  } else {
                        this._languageKeyPrefix = languageKeyPrefix;
                  }
            } else {
                  this._languageKeyPrefix = "config." + CodeHelper.getModIdFromActiveModContainer() + ".";
            }

            this._configFileName = fileName;
            this._configDirectoryName = directoryName;
            this._categories = new ArrayList();
            this._listeners = new ArrayList();
            File configDirectory = Loader.instance().getConfigDir();
            File directory = null != this._configDirectoryName ? new File(configDirectory, this._configDirectoryName) : configDirectory;
            if (!directory.exists() && !directory.mkdir()) {
                  throw new RuntimeException(String.format("Unable to create config directory %s", directory.getName()));
            } else {
                  this._configuration = new Configuration(new File(directory, this._configFileName));
                  this.loadConfigurationCategories();
                  this.addListener(this);
            }
      }

      public void sync() {
            this.loadConfigurationValues();
            if (this._configuration.hasChanged()) {
                  this._configuration.save();
            }

      }

      public void addListener(IConfigListener listener) {
            this._listeners.add(listener);
      }

      public void notifyListeners() {
            Iterator var1 = this._listeners.iterator();

            while(var1.hasNext()) {
                  IConfigListener listener = (IConfigListener)var1.next();
                  listener.onConfigChanged();
            }

      }

      public List getConfigElements() {
            List elements = new ArrayList(this._categories.size());
            Iterator var2 = this._categories.iterator();

            while(var2.hasNext()) {
                  ConfigCategory category = (ConfigCategory)var2.next();
                  elements.add(new ConfigElement(category));
            }

            return elements;
      }

      public String toString() {
            return null != this._configuration ? this._configuration.toString() : "configuration";
      }

      protected abstract void loadConfigurationCategories();

      protected abstract void loadConfigurationValues();

      protected ConfigCategory getCategory(String name) {
            return this.getCategory(name, (String)null);
      }

      protected ConfigCategory getCategory(String name, String comment) {
            ConfigCategory category = this._configuration.getCategory(name);
            if (null != comment) {
                  category.setComment(comment);
            }

            this.config(category);
            this._categories.add(category);
            return category;
      }

      protected Property getProperty(String propertyName, ConfigCategory category, boolean defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected Property getProperty(String propertyName, ConfigCategory category, boolean[] defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected boolean getValue(String propertyName, ConfigCategory category, boolean defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getBoolean();
      }

      protected boolean[] getValue(String propertyName, ConfigCategory category, boolean[] defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getBooleanList();
      }

      protected Property getProperty(String propertyName, ConfigCategory category, int defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected Property getProperty(String propertyName, ConfigCategory category, int[] defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected int getValue(String propertyName, ConfigCategory category, int defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getInt();
      }

      protected int[] getValue(String propertyName, ConfigCategory category, int[] defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getIntList();
      }

      protected Property getProperty(String propertyName, ConfigCategory category, double defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected Property getProperty(String propertyName, ConfigCategory category, double[] defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected double getValue(String propertyName, ConfigCategory category, double defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getDouble();
      }

      protected double[] getValue(String propertyName, ConfigCategory category, double[] defaultValue, String comment) {
            return this.getProperty(propertyName, category, defaultValue, comment).getDoubleList();
      }

      protected float getValue(String propertyName, ConfigCategory category, float defaultValue, String comment) {
            return (float)this.getValue(propertyName, category, (double)defaultValue, comment);
      }

      protected float[] getValue(String propertyName, ConfigCategory category, float[] defaultValue, String comment) {
            int length = defaultValue.length;
            double[] doubles = new double[length];

            int idx;
            for(idx = 0; idx < length; ++idx) {
                  doubles[idx] = (double)defaultValue[idx];
            }

            doubles = this.getValue(propertyName, category, doubles, comment);
            length = doubles.length;
            float[] floats = new float[length];

            for(idx = 0; idx < length; ++idx) {
                  floats[idx] = (float)doubles[idx];
            }

            return floats;
      }

      protected Property getProperty(String propertyName, ConfigCategory category, String defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected Property getProperty(String propertyName, ConfigCategory category, String[] defaultValue, String comment) {
            return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
      }

      protected String getValue(String propertyName, ConfigCategory category, String defaultValue, String comment) {
            String value = this.getProperty(propertyName, category, defaultValue, comment).getString();
            return null != value ? value : defaultValue;
      }

      protected String[] getValue(String propertyName, ConfigCategory category, String[] defaultValue, String comment) {
            String[] value = this.getProperty(propertyName, category, defaultValue, comment).getStringList();
            return null != value ? value : defaultValue;
      }

      private ConfigCategory config(ConfigCategory category) {
            return category.setLanguageKey(this._languageKeyPrefix + category.getName());
      }

      private Property config(Property property, ConfigCategory category) {
            return property.setLanguageKey(this._languageKeyPrefix + category.getName() + "." + property.getName());
      }
}

package com.basculasmagris.visorremotomixer.model.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SpiMixerRoomDatabase_Impl extends SpiMixerRoomDatabase {
  private volatile UserDao _userDao;

  private volatile ProductDao _productDao;

  private volatile MixerDao _mixerDao;

  private volatile EstablishmentDao _establishmentDao;

  private volatile CorralDao _corralDao;

  private volatile DietDao _dietDao;

  private volatile RoundDao _roundDao;

  private volatile RoundLocalDao _roundLocalDao;

  private volatile TabletMixerDao _tabletMixerDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(13) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user` (`username` TEXT NOT NULL, `name` TEXT NOT NULL, `lastname` TEXT NOT NULL, `mail` TEXT NOT NULL, `password` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `code_role` INTEGER NOT NULL, `code_client` TEXT NOT NULL, `id` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `product` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `specific_weight` REAL NOT NULL, `rfid` INTEGER NOT NULL, `image` TEXT NOT NULL, `imageSource` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `mixer` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `mac` TEXT NOT NULL, `bt_box` TEXT NOT NULL, `tara` REAL NOT NULL, `calibration` REAL NOT NULL, `rfid` INTEGER NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `linked` INTEGER, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `establishment` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `corral` (`establishment_id` INTEGER NOT NULL, `establishment_remote_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `animal_quantity` INTEGER NOT NULL, `rfid` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`establishment_id`) REFERENCES `establishment`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_corral_establishment_id` ON `corral` (`establishment_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `diet_product` (`diet_id` INTEGER NOT NULL, `product_id` INTEGER NOT NULL, `remote_diet_id` INTEGER NOT NULL, `remote_product_id` INTEGER NOT NULL, `order` INTEGER NOT NULL, `weight` REAL NOT NULL, `percentage` REAL NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, PRIMARY KEY(`diet_id`, `product_id`), FOREIGN KEY(`product_id`) REFERENCES `product`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`diet_id`) REFERENCES `diet`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `diet` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `user_percentage` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `round` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `weight` REAL NOT NULL, `use_percentage` INTEGER NOT NULL, `custom_percentage` REAL NOT NULL, `diet_id` INTEGER NOT NULL, `remote_diet_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`diet_id`) REFERENCES `diet`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `round_corral` (`round_id` INTEGER NOT NULL, `corral_id` INTEGER NOT NULL, `remote_round_id` INTEGER NOT NULL, `remote_corral_id` INTEGER NOT NULL, `order` INTEGER NOT NULL, `weight` REAL NOT NULL, `percentage` REAL NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, PRIMARY KEY(`round_id`, `corral_id`), FOREIGN KEY(`corral_id`) REFERENCES `corral`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`round_id`) REFERENCES `round`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `round_run_progress_load` (`round_run_id` INTEGER NOT NULL, `remote_round_run_id` INTEGER NOT NULL, `diet_id` INTEGER NOT NULL, `remote_diet_id` INTEGER NOT NULL, `product_id` INTEGER NOT NULL, `remote_product_id` INTEGER NOT NULL, `initial_weight` REAL NOT NULL, `current_weight` REAL NOT NULL, `final_weight` REAL NOT NULL, `target_weight` REAL NOT NULL, PRIMARY KEY(`round_run_id`, `diet_id`, `product_id`), FOREIGN KEY(`round_run_id`) REFERENCES `round_run`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`diet_id`) REFERENCES `diet`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`product_id`) REFERENCES `product`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `round_run_progress_download` (`round_run_id` INTEGER NOT NULL, `remote_round_run_id` INTEGER NOT NULL, `corral_id` INTEGER NOT NULL, `remote_corral_id` INTEGER NOT NULL, `initial_weight` REAL NOT NULL, `current_weight` REAL NOT NULL, `final_weight` REAL NOT NULL, `custom_target_weight` REAL NOT NULL, `actual_target_weight` REAL NOT NULL, PRIMARY KEY(`round_run_id`, `corral_id`), FOREIGN KEY(`round_run_id`) REFERENCES `round_run`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`corral_id`) REFERENCES `corral`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `round_run` (`remote_user_id` INTEGER NOT NULL, `user_display_name` TEXT NOT NULL, `round_id` INTEGER NOT NULL, `remote_round_id` INTEGER NOT NULL, `mixer_id` INTEGER NOT NULL, `remote_mixer_id` INTEGER NOT NULL, `start_date` TEXT NOT NULL, `updated_date` TEXT NOT NULL, `end_date` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `custom_percentage` REAL NOT NULL, `custom_tara` REAL NOT NULL, `added_blend` REAL NOT NULL, `state` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`round_id`) REFERENCES `round`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`mixer_id`) REFERENCES `mixer`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `RoundLocal` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT NOT NULL, `state` INTEGER NOT NULL, `remote_id` INTEGER NOT NULL, `tablet_mixer_id` INTEGER NOT NULL, `tablet_mixer_mac` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tablet_mixer` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `mac` TEXT NOT NULL, `bt_box` TEXT NOT NULL, `remote_id` INTEGER NOT NULL, `updated_date` TEXT NOT NULL, `archive_date` TEXT, `linked` INTEGER, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '331c60d8cb0029d0b624e0a10f58fc1c')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `user`");
        _db.execSQL("DROP TABLE IF EXISTS `product`");
        _db.execSQL("DROP TABLE IF EXISTS `mixer`");
        _db.execSQL("DROP TABLE IF EXISTS `establishment`");
        _db.execSQL("DROP TABLE IF EXISTS `corral`");
        _db.execSQL("DROP TABLE IF EXISTS `diet_product`");
        _db.execSQL("DROP TABLE IF EXISTS `diet`");
        _db.execSQL("DROP TABLE IF EXISTS `round`");
        _db.execSQL("DROP TABLE IF EXISTS `round_corral`");
        _db.execSQL("DROP TABLE IF EXISTS `round_run_progress_load`");
        _db.execSQL("DROP TABLE IF EXISTS `round_run_progress_download`");
        _db.execSQL("DROP TABLE IF EXISTS `round_run`");
        _db.execSQL("DROP TABLE IF EXISTS `RoundLocal`");
        _db.execSQL("DROP TABLE IF EXISTS `tablet_mixer`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(11);
        _columnsUser.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("lastname", new TableInfo.Column("lastname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("mail", new TableInfo.Column("mail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("code_role", new TableInfo.Column("code_role", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("code_client", new TableInfo.Column("code_client", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("user", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(_db, "user");
        if (! _infoUser.equals(_existingUser)) {
          return new RoomOpenHelper.ValidationResult(false, "user(com.basculasmagris.visorremotomixer.model.entities.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        final HashMap<String, TableInfo.Column> _columnsProduct = new HashMap<String, TableInfo.Column>(10);
        _columnsProduct.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("specific_weight", new TableInfo.Column("specific_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("rfid", new TableInfo.Column("rfid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("image", new TableInfo.Column("image", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("imageSource", new TableInfo.Column("imageSource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProduct.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProduct = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProduct = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProduct = new TableInfo("product", _columnsProduct, _foreignKeysProduct, _indicesProduct);
        final TableInfo _existingProduct = TableInfo.read(_db, "product");
        if (! _infoProduct.equals(_existingProduct)) {
          return new RoomOpenHelper.ValidationResult(false, "product(com.basculasmagris.visorremotomixer.model.entities.Product).\n"
                  + " Expected:\n" + _infoProduct + "\n"
                  + " Found:\n" + _existingProduct);
        }
        final HashMap<String, TableInfo.Column> _columnsMixer = new HashMap<String, TableInfo.Column>(12);
        _columnsMixer.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("mac", new TableInfo.Column("mac", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("bt_box", new TableInfo.Column("bt_box", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("tara", new TableInfo.Column("tara", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("calibration", new TableInfo.Column("calibration", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("rfid", new TableInfo.Column("rfid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("linked", new TableInfo.Column("linked", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMixer.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMixer = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMixer = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMixer = new TableInfo("mixer", _columnsMixer, _foreignKeysMixer, _indicesMixer);
        final TableInfo _existingMixer = TableInfo.read(_db, "mixer");
        if (! _infoMixer.equals(_existingMixer)) {
          return new RoomOpenHelper.ValidationResult(false, "mixer(com.basculasmagris.visorremotomixer.model.entities.Mixer).\n"
                  + " Expected:\n" + _infoMixer + "\n"
                  + " Found:\n" + _existingMixer);
        }
        final HashMap<String, TableInfo.Column> _columnsEstablishment = new HashMap<String, TableInfo.Column>(6);
        _columnsEstablishment.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablishment.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablishment.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablishment.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablishment.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablishment.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEstablishment = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEstablishment = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEstablishment = new TableInfo("establishment", _columnsEstablishment, _foreignKeysEstablishment, _indicesEstablishment);
        final TableInfo _existingEstablishment = TableInfo.read(_db, "establishment");
        if (! _infoEstablishment.equals(_existingEstablishment)) {
          return new RoomOpenHelper.ValidationResult(false, "establishment(com.basculasmagris.visorremotomixer.model.entities.Establishment).\n"
                  + " Expected:\n" + _infoEstablishment + "\n"
                  + " Found:\n" + _existingEstablishment);
        }
        final HashMap<String, TableInfo.Column> _columnsCorral = new HashMap<String, TableInfo.Column>(10);
        _columnsCorral.put("establishment_id", new TableInfo.Column("establishment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("establishment_remote_id", new TableInfo.Column("establishment_remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("animal_quantity", new TableInfo.Column("animal_quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("rfid", new TableInfo.Column("rfid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCorral.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCorral = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCorral.add(new TableInfo.ForeignKey("establishment", "NO ACTION", "NO ACTION",Arrays.asList("establishment_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCorral = new HashSet<TableInfo.Index>(1);
        _indicesCorral.add(new TableInfo.Index("index_corral_establishment_id", false, Arrays.asList("establishment_id"), Arrays.asList("ASC")));
        final TableInfo _infoCorral = new TableInfo("corral", _columnsCorral, _foreignKeysCorral, _indicesCorral);
        final TableInfo _existingCorral = TableInfo.read(_db, "corral");
        if (! _infoCorral.equals(_existingCorral)) {
          return new RoomOpenHelper.ValidationResult(false, "corral(com.basculasmagris.visorremotomixer.model.entities.Corral).\n"
                  + " Expected:\n" + _infoCorral + "\n"
                  + " Found:\n" + _existingCorral);
        }
        final HashMap<String, TableInfo.Column> _columnsDietProduct = new HashMap<String, TableInfo.Column>(10);
        _columnsDietProduct.put("diet_id", new TableInfo.Column("diet_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("product_id", new TableInfo.Column("product_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("remote_diet_id", new TableInfo.Column("remote_diet_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("remote_product_id", new TableInfo.Column("remote_product_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("percentage", new TableInfo.Column("percentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDietProduct.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDietProduct = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDietProduct.add(new TableInfo.ForeignKey("product", "NO ACTION", "NO ACTION",Arrays.asList("product_id"), Arrays.asList("id")));
        _foreignKeysDietProduct.add(new TableInfo.ForeignKey("diet", "NO ACTION", "NO ACTION",Arrays.asList("diet_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDietProduct = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDietProduct = new TableInfo("diet_product", _columnsDietProduct, _foreignKeysDietProduct, _indicesDietProduct);
        final TableInfo _existingDietProduct = TableInfo.read(_db, "diet_product");
        if (! _infoDietProduct.equals(_existingDietProduct)) {
          return new RoomOpenHelper.ValidationResult(false, "diet_product(com.basculasmagris.visorremotomixer.model.entities.DietProduct).\n"
                  + " Expected:\n" + _infoDietProduct + "\n"
                  + " Found:\n" + _existingDietProduct);
        }
        final HashMap<String, TableInfo.Column> _columnsDiet = new HashMap<String, TableInfo.Column>(7);
        _columnsDiet.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("user_percentage", new TableInfo.Column("user_percentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiet.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiet = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiet = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiet = new TableInfo("diet", _columnsDiet, _foreignKeysDiet, _indicesDiet);
        final TableInfo _existingDiet = TableInfo.read(_db, "diet");
        if (! _infoDiet.equals(_existingDiet)) {
          return new RoomOpenHelper.ValidationResult(false, "diet(com.basculasmagris.visorremotomixer.model.entities.Diet).\n"
                  + " Expected:\n" + _infoDiet + "\n"
                  + " Found:\n" + _existingDiet);
        }
        final HashMap<String, TableInfo.Column> _columnsRound = new HashMap<String, TableInfo.Column>(11);
        _columnsRound.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("use_percentage", new TableInfo.Column("use_percentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("custom_percentage", new TableInfo.Column("custom_percentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("diet_id", new TableInfo.Column("diet_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("remote_diet_id", new TableInfo.Column("remote_diet_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRound.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRound = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRound.add(new TableInfo.ForeignKey("diet", "NO ACTION", "NO ACTION",Arrays.asList("diet_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRound = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRound = new TableInfo("round", _columnsRound, _foreignKeysRound, _indicesRound);
        final TableInfo _existingRound = TableInfo.read(_db, "round");
        if (! _infoRound.equals(_existingRound)) {
          return new RoomOpenHelper.ValidationResult(false, "round(com.basculasmagris.visorremotomixer.model.entities.Round).\n"
                  + " Expected:\n" + _infoRound + "\n"
                  + " Found:\n" + _existingRound);
        }
        final HashMap<String, TableInfo.Column> _columnsRoundCorral = new HashMap<String, TableInfo.Column>(10);
        _columnsRoundCorral.put("round_id", new TableInfo.Column("round_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("corral_id", new TableInfo.Column("corral_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("remote_round_id", new TableInfo.Column("remote_round_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("remote_corral_id", new TableInfo.Column("remote_corral_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("percentage", new TableInfo.Column("percentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundCorral.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoundCorral = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRoundCorral.add(new TableInfo.ForeignKey("corral", "NO ACTION", "NO ACTION",Arrays.asList("corral_id"), Arrays.asList("id")));
        _foreignKeysRoundCorral.add(new TableInfo.ForeignKey("round", "CASCADE", "NO ACTION",Arrays.asList("round_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRoundCorral = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoundCorral = new TableInfo("round_corral", _columnsRoundCorral, _foreignKeysRoundCorral, _indicesRoundCorral);
        final TableInfo _existingRoundCorral = TableInfo.read(_db, "round_corral");
        if (! _infoRoundCorral.equals(_existingRoundCorral)) {
          return new RoomOpenHelper.ValidationResult(false, "round_corral(com.basculasmagris.visorremotomixer.model.entities.RoundCorral).\n"
                  + " Expected:\n" + _infoRoundCorral + "\n"
                  + " Found:\n" + _existingRoundCorral);
        }
        final HashMap<String, TableInfo.Column> _columnsRoundRunProgressLoad = new HashMap<String, TableInfo.Column>(10);
        _columnsRoundRunProgressLoad.put("round_run_id", new TableInfo.Column("round_run_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("remote_round_run_id", new TableInfo.Column("remote_round_run_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("diet_id", new TableInfo.Column("diet_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("remote_diet_id", new TableInfo.Column("remote_diet_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("product_id", new TableInfo.Column("product_id", "INTEGER", true, 3, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("remote_product_id", new TableInfo.Column("remote_product_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("initial_weight", new TableInfo.Column("initial_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("current_weight", new TableInfo.Column("current_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("final_weight", new TableInfo.Column("final_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressLoad.put("target_weight", new TableInfo.Column("target_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoundRunProgressLoad = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysRoundRunProgressLoad.add(new TableInfo.ForeignKey("round_run", "CASCADE", "NO ACTION",Arrays.asList("round_run_id"), Arrays.asList("id")));
        _foreignKeysRoundRunProgressLoad.add(new TableInfo.ForeignKey("diet", "NO ACTION", "NO ACTION",Arrays.asList("diet_id"), Arrays.asList("id")));
        _foreignKeysRoundRunProgressLoad.add(new TableInfo.ForeignKey("product", "NO ACTION", "NO ACTION",Arrays.asList("product_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRoundRunProgressLoad = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoundRunProgressLoad = new TableInfo("round_run_progress_load", _columnsRoundRunProgressLoad, _foreignKeysRoundRunProgressLoad, _indicesRoundRunProgressLoad);
        final TableInfo _existingRoundRunProgressLoad = TableInfo.read(_db, "round_run_progress_load");
        if (! _infoRoundRunProgressLoad.equals(_existingRoundRunProgressLoad)) {
          return new RoomOpenHelper.ValidationResult(false, "round_run_progress_load(com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad).\n"
                  + " Expected:\n" + _infoRoundRunProgressLoad + "\n"
                  + " Found:\n" + _existingRoundRunProgressLoad);
        }
        final HashMap<String, TableInfo.Column> _columnsRoundRunProgressDownload = new HashMap<String, TableInfo.Column>(9);
        _columnsRoundRunProgressDownload.put("round_run_id", new TableInfo.Column("round_run_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("remote_round_run_id", new TableInfo.Column("remote_round_run_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("corral_id", new TableInfo.Column("corral_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("remote_corral_id", new TableInfo.Column("remote_corral_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("initial_weight", new TableInfo.Column("initial_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("current_weight", new TableInfo.Column("current_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("final_weight", new TableInfo.Column("final_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("custom_target_weight", new TableInfo.Column("custom_target_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRunProgressDownload.put("actual_target_weight", new TableInfo.Column("actual_target_weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoundRunProgressDownload = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRoundRunProgressDownload.add(new TableInfo.ForeignKey("round_run", "CASCADE", "NO ACTION",Arrays.asList("round_run_id"), Arrays.asList("id")));
        _foreignKeysRoundRunProgressDownload.add(new TableInfo.ForeignKey("corral", "NO ACTION", "NO ACTION",Arrays.asList("corral_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRoundRunProgressDownload = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoundRunProgressDownload = new TableInfo("round_run_progress_download", _columnsRoundRunProgressDownload, _foreignKeysRoundRunProgressDownload, _indicesRoundRunProgressDownload);
        final TableInfo _existingRoundRunProgressDownload = TableInfo.read(_db, "round_run_progress_download");
        if (! _infoRoundRunProgressDownload.equals(_existingRoundRunProgressDownload)) {
          return new RoomOpenHelper.ValidationResult(false, "round_run_progress_download(com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload).\n"
                  + " Expected:\n" + _infoRoundRunProgressDownload + "\n"
                  + " Found:\n" + _existingRoundRunProgressDownload);
        }
        final HashMap<String, TableInfo.Column> _columnsRoundRun = new HashMap<String, TableInfo.Column>(15);
        _columnsRoundRun.put("remote_user_id", new TableInfo.Column("remote_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("user_display_name", new TableInfo.Column("user_display_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("round_id", new TableInfo.Column("round_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("remote_round_id", new TableInfo.Column("remote_round_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("mixer_id", new TableInfo.Column("mixer_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("remote_mixer_id", new TableInfo.Column("remote_mixer_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("start_date", new TableInfo.Column("start_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("end_date", new TableInfo.Column("end_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("custom_percentage", new TableInfo.Column("custom_percentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("custom_tara", new TableInfo.Column("custom_tara", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("added_blend", new TableInfo.Column("added_blend", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("state", new TableInfo.Column("state", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundRun.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoundRun = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRoundRun.add(new TableInfo.ForeignKey("round", "CASCADE", "NO ACTION",Arrays.asList("round_id"), Arrays.asList("id")));
        _foreignKeysRoundRun.add(new TableInfo.ForeignKey("mixer", "NO ACTION", "NO ACTION",Arrays.asList("mixer_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRoundRun = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoundRun = new TableInfo("round_run", _columnsRoundRun, _foreignKeysRoundRun, _indicesRoundRun);
        final TableInfo _existingRoundRun = TableInfo.read(_db, "round_run");
        if (! _infoRoundRun.equals(_existingRoundRun)) {
          return new RoomOpenHelper.ValidationResult(false, "round_run(com.basculasmagris.visorremotomixer.model.entities.RoundRun).\n"
                  + " Expected:\n" + _infoRoundRun + "\n"
                  + " Found:\n" + _existingRoundRun);
        }
        final HashMap<String, TableInfo.Column> _columnsRoundLocal = new HashMap<String, TableInfo.Column>(9);
        _columnsRoundLocal.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("endDate", new TableInfo.Column("endDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("state", new TableInfo.Column("state", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("tablet_mixer_id", new TableInfo.Column("tablet_mixer_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("tablet_mixer_mac", new TableInfo.Column("tablet_mixer_mac", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoundLocal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoundLocal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoundLocal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoundLocal = new TableInfo("RoundLocal", _columnsRoundLocal, _foreignKeysRoundLocal, _indicesRoundLocal);
        final TableInfo _existingRoundLocal = TableInfo.read(_db, "RoundLocal");
        if (! _infoRoundLocal.equals(_existingRoundLocal)) {
          return new RoomOpenHelper.ValidationResult(false, "RoundLocal(com.basculasmagris.visorremotomixer.model.entities.RoundLocal).\n"
                  + " Expected:\n" + _infoRoundLocal + "\n"
                  + " Found:\n" + _existingRoundLocal);
        }
        final HashMap<String, TableInfo.Column> _columnsTabletMixer = new HashMap<String, TableInfo.Column>(9);
        _columnsTabletMixer.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("mac", new TableInfo.Column("mac", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("bt_box", new TableInfo.Column("bt_box", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("remote_id", new TableInfo.Column("remote_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("updated_date", new TableInfo.Column("updated_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("archive_date", new TableInfo.Column("archive_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("linked", new TableInfo.Column("linked", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTabletMixer.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTabletMixer = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTabletMixer = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTabletMixer = new TableInfo("tablet_mixer", _columnsTabletMixer, _foreignKeysTabletMixer, _indicesTabletMixer);
        final TableInfo _existingTabletMixer = TableInfo.read(_db, "tablet_mixer");
        if (! _infoTabletMixer.equals(_existingTabletMixer)) {
          return new RoomOpenHelper.ValidationResult(false, "tablet_mixer(com.basculasmagris.visorremotomixer.model.entities.TabletMixer).\n"
                  + " Expected:\n" + _infoTabletMixer + "\n"
                  + " Found:\n" + _existingTabletMixer);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "331c60d8cb0029d0b624e0a10f58fc1c", "3ab3b3f55c7957ab060bb7e04ee5e74d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user","product","mixer","establishment","corral","diet_product","diet","round","round_corral","round_run_progress_load","round_run_progress_download","round_run","RoundLocal","tablet_mixer");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `user`");
      _db.execSQL("DELETE FROM `product`");
      _db.execSQL("DELETE FROM `mixer`");
      _db.execSQL("DELETE FROM `corral`");
      _db.execSQL("DELETE FROM `establishment`");
      _db.execSQL("DELETE FROM `diet_product`");
      _db.execSQL("DELETE FROM `round`");
      _db.execSQL("DELETE FROM `round_run_progress_load`");
      _db.execSQL("DELETE FROM `diet`");
      _db.execSQL("DELETE FROM `round_corral`");
      _db.execSQL("DELETE FROM `round_run_progress_download`");
      _db.execSQL("DELETE FROM `round_run`");
      _db.execSQL("DELETE FROM `RoundLocal`");
      _db.execSQL("DELETE FROM `tablet_mixer`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MixerDao.class, MixerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EstablishmentDao.class, EstablishmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CorralDao.class, CorralDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DietDao.class, DietDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoundDao.class, RoundDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoundLocalDao.class, RoundLocalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TabletMixerDao.class, TabletMixerDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public MixerDao mixerDao() {
    if (_mixerDao != null) {
      return _mixerDao;
    } else {
      synchronized(this) {
        if(_mixerDao == null) {
          _mixerDao = new MixerDao_Impl(this);
        }
        return _mixerDao;
      }
    }
  }

  @Override
  public EstablishmentDao establishmentDao() {
    if (_establishmentDao != null) {
      return _establishmentDao;
    } else {
      synchronized(this) {
        if(_establishmentDao == null) {
          _establishmentDao = new EstablishmentDao_Impl(this);
        }
        return _establishmentDao;
      }
    }
  }

  @Override
  public CorralDao corralDao() {
    if (_corralDao != null) {
      return _corralDao;
    } else {
      synchronized(this) {
        if(_corralDao == null) {
          _corralDao = new CorralDao_Impl(this);
        }
        return _corralDao;
      }
    }
  }

  @Override
  public DietDao dietDao() {
    if (_dietDao != null) {
      return _dietDao;
    } else {
      synchronized(this) {
        if(_dietDao == null) {
          _dietDao = new DietDao_Impl(this);
        }
        return _dietDao;
      }
    }
  }

  @Override
  public RoundDao roundDao() {
    if (_roundDao != null) {
      return _roundDao;
    } else {
      synchronized(this) {
        if(_roundDao == null) {
          _roundDao = new RoundDao_Impl(this);
        }
        return _roundDao;
      }
    }
  }

  @Override
  public RoundLocalDao roundLocalDao() {
    if (_roundLocalDao != null) {
      return _roundLocalDao;
    } else {
      synchronized(this) {
        if(_roundLocalDao == null) {
          _roundLocalDao = new RoundLocalDao_Impl(this);
        }
        return _roundLocalDao;
      }
    }
  }

  @Override
  public TabletMixerDao remoteViewerDao() {
    if (_tabletMixerDao != null) {
      return _tabletMixerDao;
    } else {
      synchronized(this) {
        if(_tabletMixerDao == null) {
          _tabletMixerDao = new TabletMixerDao_Impl(this);
        }
        return _tabletMixerDao;
      }
    }
  }
}

package com.example.drop.wzjtestapp.database.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.drop.wzjtestapp.database.db.StockProductSave;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "STOCK_PRODUCT_SAVE".
*/
public class StockProductSaveDao extends AbstractDao<StockProductSave, Long> {

    public static final String TABLENAME = "STOCK_PRODUCT_SAVE";

    /**
     * Properties of entity StockProductSave.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Productid = new Property(1, String.class, "productid", false, "PRODUCTID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Barcode = new Property(3, String.class, "barcode", false, "BARCODE");
        public final static Property Shortcode = new Property(4, String.class, "shortcode", false, "SHORTCODE");
        public final static Property Units = new Property(5, String.class, "units", false, "UNITS");
        public final static Property Spec = new Property(6, String.class, "spec", false, "SPEC");
        public final static Property Alianame = new Property(7, String.class, "alianame", false, "ALIANAME");
        public final static Property Stockproperty = new Property(8, String.class, "stockproperty", false, "STOCKPROPERTY");
        public final static Property Storecode = new Property(9, String.class, "storecode", false, "STORECODE");
        public final static Property Makedate = new Property(10, String.class, "makedate", false, "MAKEDATE");
        public final static Property Maker = new Property(11, String.class, "maker", false, "MAKER");
        public final static Property Editmark = new Property(12, String.class, "editmark", false, "EDITMARK");
        public final static Property Edittime = new Property(13, String.class, "edittime", false, "EDITTIME");
        public final static Property Goodstypecode = new Property(14, String.class, "goodstypecode", false, "GOODSTYPECODE");
        public final static Property Brandid = new Property(15, String.class, "brandid", false, "BRANDID");
        public final static Property Isbulk = new Property(16, String.class, "isbulk", false, "ISBULK");
        public final static Property Realquantity = new Property(17, String.class, "realquantity", false, "REALQUANTITY");
        public final static Property Crno = new Property(18, String.class, "crno", false, "CRNO");
        public final static Property Local = new Property(19, String.class, "local", false, "LOCAL");
    }


    public StockProductSaveDao(DaoConfig config) {
        super(config);
    }
    
    public StockProductSaveDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STOCK_PRODUCT_SAVE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"PRODUCTID\" TEXT," + // 1: productid
                "\"NAME\" TEXT," + // 2: name
                "\"BARCODE\" TEXT," + // 3: barcode
                "\"SHORTCODE\" TEXT," + // 4: shortcode
                "\"UNITS\" TEXT," + // 5: units
                "\"SPEC\" TEXT," + // 6: spec
                "\"ALIANAME\" TEXT," + // 7: alianame
                "\"STOCKPROPERTY\" TEXT," + // 8: stockproperty
                "\"STORECODE\" TEXT," + // 9: storecode
                "\"MAKEDATE\" TEXT," + // 10: makedate
                "\"MAKER\" TEXT," + // 11: maker
                "\"EDITMARK\" TEXT," + // 12: editmark
                "\"EDITTIME\" TEXT," + // 13: edittime
                "\"GOODSTYPECODE\" TEXT," + // 14: goodstypecode
                "\"BRANDID\" TEXT," + // 15: brandid
                "\"ISBULK\" TEXT," + // 16: isbulk
                "\"REALQUANTITY\" TEXT," + // 17: realquantity
                "\"CRNO\" TEXT," + // 18: crno
                "\"LOCAL\" TEXT);"); // 19: local
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STOCK_PRODUCT_SAVE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StockProductSave entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String productid = entity.getProductid();
        if (productid != null) {
            stmt.bindString(2, productid);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String barcode = entity.getBarcode();
        if (barcode != null) {
            stmt.bindString(4, barcode);
        }
 
        String shortcode = entity.getShortcode();
        if (shortcode != null) {
            stmt.bindString(5, shortcode);
        }
 
        String units = entity.getUnits();
        if (units != null) {
            stmt.bindString(6, units);
        }
 
        String spec = entity.getSpec();
        if (spec != null) {
            stmt.bindString(7, spec);
        }
 
        String alianame = entity.getAlianame();
        if (alianame != null) {
            stmt.bindString(8, alianame);
        }
 
        String stockproperty = entity.getStockproperty();
        if (stockproperty != null) {
            stmt.bindString(9, stockproperty);
        }
 
        String storecode = entity.getStorecode();
        if (storecode != null) {
            stmt.bindString(10, storecode);
        }
 
        String makedate = entity.getMakedate();
        if (makedate != null) {
            stmt.bindString(11, makedate);
        }
 
        String maker = entity.getMaker();
        if (maker != null) {
            stmt.bindString(12, maker);
        }
 
        String editmark = entity.getEditmark();
        if (editmark != null) {
            stmt.bindString(13, editmark);
        }
 
        String edittime = entity.getEdittime();
        if (edittime != null) {
            stmt.bindString(14, edittime);
        }
 
        String goodstypecode = entity.getGoodstypecode();
        if (goodstypecode != null) {
            stmt.bindString(15, goodstypecode);
        }
 
        String brandid = entity.getBrandid();
        if (brandid != null) {
            stmt.bindString(16, brandid);
        }
 
        String isbulk = entity.getIsbulk();
        if (isbulk != null) {
            stmt.bindString(17, isbulk);
        }
 
        String realquantity = entity.getRealquantity();
        if (realquantity != null) {
            stmt.bindString(18, realquantity);
        }
 
        String crno = entity.getCrno();
        if (crno != null) {
            stmt.bindString(19, crno);
        }
 
        String local = entity.getLocal();
        if (local != null) {
            stmt.bindString(20, local);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StockProductSave entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String productid = entity.getProductid();
        if (productid != null) {
            stmt.bindString(2, productid);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String barcode = entity.getBarcode();
        if (barcode != null) {
            stmt.bindString(4, barcode);
        }
 
        String shortcode = entity.getShortcode();
        if (shortcode != null) {
            stmt.bindString(5, shortcode);
        }
 
        String units = entity.getUnits();
        if (units != null) {
            stmt.bindString(6, units);
        }
 
        String spec = entity.getSpec();
        if (spec != null) {
            stmt.bindString(7, spec);
        }
 
        String alianame = entity.getAlianame();
        if (alianame != null) {
            stmt.bindString(8, alianame);
        }
 
        String stockproperty = entity.getStockproperty();
        if (stockproperty != null) {
            stmt.bindString(9, stockproperty);
        }
 
        String storecode = entity.getStorecode();
        if (storecode != null) {
            stmt.bindString(10, storecode);
        }
 
        String makedate = entity.getMakedate();
        if (makedate != null) {
            stmt.bindString(11, makedate);
        }
 
        String maker = entity.getMaker();
        if (maker != null) {
            stmt.bindString(12, maker);
        }
 
        String editmark = entity.getEditmark();
        if (editmark != null) {
            stmt.bindString(13, editmark);
        }
 
        String edittime = entity.getEdittime();
        if (edittime != null) {
            stmt.bindString(14, edittime);
        }
 
        String goodstypecode = entity.getGoodstypecode();
        if (goodstypecode != null) {
            stmt.bindString(15, goodstypecode);
        }
 
        String brandid = entity.getBrandid();
        if (brandid != null) {
            stmt.bindString(16, brandid);
        }
 
        String isbulk = entity.getIsbulk();
        if (isbulk != null) {
            stmt.bindString(17, isbulk);
        }
 
        String realquantity = entity.getRealquantity();
        if (realquantity != null) {
            stmt.bindString(18, realquantity);
        }
 
        String crno = entity.getCrno();
        if (crno != null) {
            stmt.bindString(19, crno);
        }
 
        String local = entity.getLocal();
        if (local != null) {
            stmt.bindString(20, local);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public StockProductSave readEntity(Cursor cursor, int offset) {
        StockProductSave entity = new StockProductSave( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // productid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // barcode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shortcode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // units
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // spec
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // alianame
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // stockproperty
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // storecode
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // makedate
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // maker
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // editmark
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // edittime
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // goodstypecode
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // brandid
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // isbulk
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // realquantity
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // crno
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19) // local
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StockProductSave entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProductid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBarcode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShortcode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUnits(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSpec(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAlianame(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStockproperty(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStorecode(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setMakedate(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setMaker(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setEditmark(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setEdittime(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setGoodstypecode(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setBrandid(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setIsbulk(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setRealquantity(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setCrno(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setLocal(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(StockProductSave entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(StockProductSave entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(StockProductSave entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

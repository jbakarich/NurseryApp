package com.MispronouncedDevelopment.Homeroom;

import android.provider.BaseColumns;

/**
 * Created by jacob on 9/17/2016.
 */






public abstract class AA_TableData implements BaseColumns {



        private AA_TableData()
        {

        }



        public static abstract class TableInfo implements BaseColumns{

        public static final String DATABASE_NAME = "Profiles";
        public static final String TABLE_NAME = "profile_table";
        public static final String COL_2 = "USER_NAME";
        public static final String COL_3 = "PIN";
        public static final String COL_4 = "NAME";
        public static final String COL_5 = "TYPE";
    }

}

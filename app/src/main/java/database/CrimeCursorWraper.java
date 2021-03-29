package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.pandinu.criminalintent.Models.Crime;

import java.sql.Date;
import java.util.UUID;

public class CrimeCursorWraper extends CursorWrapper {
    public CrimeCursorWraper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        int policeReq = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.POLICE));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setmTitle(title);
        crime.setmDate(new Date(date));
        crime.setmSolved(isSolved!=0);
        crime.setmRequiresPolice(policeReq!=0);

        return crime;
    }
}

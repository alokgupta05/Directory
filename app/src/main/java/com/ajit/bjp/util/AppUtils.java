package com.ajit.bjp.util;

import com.ajit.bjp.model.Gs$cell;
import com.ajit.bjp.model.KaryaKarta;
import com.ajit.bjp.model.KaryaKartaEntry;
import com.ajit.bjp.model.KaryaKartaFeed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class AppUtils {

    public static List<KaryaKarta> createKaryakartaList(KaryaKartaFeed feed) {
        List<KaryaKartaEntry> entryList = feed.getEntry();
        List<KaryaKarta> list = new ArrayList<>();

        int startIndex = 0;
        for (int i = 0; i < entryList.size(); i++) {
            if(entryList.get(i).getGs$cell().getRow() > 1) {
                startIndex = i;
                break;
            }
        }

        int row = 2;

        while (startIndex < (entryList.size() - feed.getGs$colCount().get$t())) {
            KaryaKarta karyaKarta = new KaryaKarta();

            for (int i=startIndex; i<entryList.size(); i++) {

                Gs$cell cell = entryList.get(i).getGs$cell();

                if(cell.getRow() > row) {
                    startIndex = i;
                    break;

                } else {

                    switch (cell.getCol()) {

                        case 2:
                            karyaKarta.setFullName(cell.getInputValue());
                            break;

                        case 3:
                            try {
                                Date dob = new SimpleDateFormat("m/d/yyyy", Locale.ENGLISH).parse(cell.getInputValue());
                                karyaKarta.setDob(dob);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 4:
                            karyaKarta.setVillageName(cell.getInputValue());
                            break;

                        case 7:
                            karyaKarta.setMobileNo(cell.getInputValue());
                            break;

                        case 8:
                            karyaKarta.setWhatsAppNo(cell.getInputValue());
                            break;
                    }
                }
            }

            row++;
            list.add(karyaKarta);

        }

        AppCache.INSTANCE.addToAppCache(AppConstants.RESPONSE_DATA_KARYAKARTA, feed);
        AppCache.INSTANCE.addToAppCache(AppConstants.KARYAKARTA_LIST, list);

        return list;
    }
}

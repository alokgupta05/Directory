package com.ajit.bjp.util;

import com.ajit.bjp.model.CellEntry;
import com.ajit.bjp.model.Content;
import com.ajit.bjp.model.Gs$cell;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.model.karyakarta.KaryaKartaFeed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class AppUtils {

    public static List<KaryaKarta> createKaryakartaList(KaryaKartaFeed feed) {
        List<CellEntry> entryList = feed.getEntry();
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

                CellEntry entry = entryList.get(i);
                Gs$cell cell = entry.getGs$cell();
                Content content = entry.getContent();

                if(cell.getRow() > row) {
                    startIndex = i;
                    break;

                } else {

                    String text = content.get$t();
                    switch (cell.getCol()) {

                        case 2:
                            karyaKarta.setFullName(text);
                            break;

                        case 3:
                            try {
                                Date dob = new SimpleDateFormat("m/d/yyyy", Locale.ENGLISH).parse(text);
                                karyaKarta.setDob(dob);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 4:
                            karyaKarta.setVillageName(text);
                            break;

                        case 7:
                            karyaKarta.setMobileNo(text);
                            break;

                        case 8:
                            karyaKarta.setWhatsAppNo(text);
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

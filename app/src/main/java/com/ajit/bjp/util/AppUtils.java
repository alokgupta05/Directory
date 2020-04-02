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
        List<String> karyakartaListHeaders = new ArrayList<>();

        int startIndex = 0;
        for (int i = 0; i < entryList.size(); i++) {
            Gs$cell cell = entryList.get(i).getGs$cell();

            if(cell.getRow() > 1) {
                startIndex = i;
                break;
            } else {
                karyakartaListHeaders.add(cell.get$t());
            }
        }

        int row = 2;
        int entries = karyakartaListHeaders.size();

        while (entries < entryList.size()) {
            KaryaKarta karyaKarta = new KaryaKarta();

            for (int i=startIndex; i<entryList.size(); i++) {

                CellEntry entry = entryList.get(i);
                Gs$cell cell = entry.getGs$cell();
                Content content = entry.getContent();

                if(cell.getRow() > row) {
                    startIndex = i;
                    break;

                } else {

                    entries += 1;
                    String text = content.get$t();

                    switch (cell.getCol()) {

                        case 2:
                            karyaKarta.setFullName(text);
                            break;

                        case 3:
                            try {
                                Date dob = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH).parse(text);
                                karyaKarta.setDob(dob);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 4:
                            karyaKarta.setVillageName(text);
                            break;

                        case 5:
                            karyaKarta.setOccupation(text);
                            break;

                        case 6:
                            karyaKarta.setBloodGroup(text);
                            break;

                        case 7:
                            karyaKarta.setMobileNo(text);
                            break;

                        case 8:
                            karyaKarta.setWhatsAppNo(text);
                            break;

                        case 9:
                            karyaKarta.setFamilyHead(text);
                            break;

                        case 10:
                            karyaKarta.setWadiWastiName(text);
                            break;

                        case 11:
                            karyaKarta.setGramPanchayatWardNo(text);
                            break;

                        case 12:
                            karyaKarta.setVidhanSabhaWardNo(text);
                            break;

                        case 14:
                            karyaKarta.setInformation(text);
                            break;

                    }
                }
            }

            row++;
            list.add(karyaKarta);

        }

        AppCache.INSTANCE.addToAppCache(AppConstants.RESPONSE_DATA_KARYAKARTA, feed);
        AppCache.INSTANCE.addToAppCache(AppConstants.KARYAKARTA_LIST, list);
        AppCache.INSTANCE.addToAppCache(AppConstants.KARYAKARTA_LIST_HEADERS, karyakartaListHeaders);

        return list;
    }

}

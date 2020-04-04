package com.ajit.bjp.activity.karyakarta;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;
import com.ajit.bjp.util.AppUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class KarykartaItemFragment extends Fragment {

    static final String KARYAKARTA_DATA = "karyakarta_data";
    private KaryaKarta mKaryaKarta;
    private List<String> mHeaders;
    private Activity mActivity;

    public KarykartaItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null && args.containsKey(KARYAKARTA_DATA)) {
            mKaryaKarta = args.getParcelable(KARYAKARTA_DATA);
        }
        mActivity = getActivity();
        mHeaders = (List<String>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST_HEADERS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karykarta_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)view.findViewById(R.id.txtKarykartaName)).setText(mKaryaKarta.getFullName());

        ((TextView)view.findViewById(R.id.lblMobileNo)).setText(mHeaders.get(6));
        ((TextView)view.findViewById(R.id.txtMobileNo)).setText(mKaryaKarta.getMobileNo());

        ((TextView)view.findViewById(R.id.lblWhatsAppNo)).setText(mHeaders.get(7));
        ((TextView)view.findViewById(R.id.txtWhatsAppNo)).setText(mKaryaKarta.getWhatsAppNo());

        ((TextView)view.findViewById(R.id.lblVillageName)).setText(mHeaders.get(3));
        ((TextView)view.findViewById(R.id.txtVillageName)).setText(mKaryaKarta.getVillageName());

        ((TextView)view.findViewById(R.id.lblWadiWastiName)).setText(mHeaders.get(9));
        ((TextView)view.findViewById(R.id.txtWadiWastiName)).setText(mKaryaKarta.getWadiWastiName());

        ((TextView)view.findViewById(R.id.lblDOB)).setText(mHeaders.get(2));
        String dob = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(mKaryaKarta.getDob());
        ((TextView)view.findViewById(R.id.txtDOB)).setText(dob);

        ((TextView)view.findViewById(R.id.lblOccupation)).setText(mHeaders.get(4));
        ((TextView)view.findViewById(R.id.txtOccupation)).setText(mKaryaKarta.getOccupation());

        ((TextView)view.findViewById(R.id.lblBloodGroup)).setText(mHeaders.get(5));
        ((TextView)view.findViewById(R.id.txtBloodGroup)).setText(mKaryaKarta.getBloodGroup());

        ((TextView)view.findViewById(R.id.lblFamilyHead)).setText(mHeaders.get(8));
        ((TextView)view.findViewById(R.id.txtFamilyHead)).setText(mKaryaKarta.getFamilyHead());

        ((TextView)view.findViewById(R.id.lblGramPanchayatWardNo)).setText(mHeaders.get(10));
        ((TextView)view.findViewById(R.id.txtGramPanchayatWardNo)).setText(mKaryaKarta.getGramPanchayatWardNo());

        ((TextView)view.findViewById(R.id.lblVidhanSabhaWardNo)).setText(mHeaders.get(11));
        ((TextView)view.findViewById(R.id.txtVidhanSabhaWardNo)).setText(mKaryaKarta.getVidhanSabhaWardNo());

        ((TextView)view.findViewById(R.id.lblInfo)).setText(mHeaders.get(13));
        ((TextView)view.findViewById(R.id.txtInfo)).setText(mKaryaKarta.getInformation());

        view.findViewById(R.id.btnCall).setOnClickListener(view1 -> {
            String number = mKaryaKarta.getMobileNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            AppUtils.dialNumber(mActivity, number);
        });

        view.findViewById(R.id.btnSms).setOnClickListener(view1 -> {
            String number = mKaryaKarta.getMobileNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            AppUtils.openSMS(mActivity, number);
        });

        view.findViewById(R.id.btnWhatsApp).setOnClickListener(view1 -> {
            String number = mKaryaKarta.getWhatsAppNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            AppUtils.openWhatsApp(mActivity, number);
        });

    }
}

package com.example.docportal.Patient;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.HelperFunctions;
import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class labTestManagement_options extends AppCompatActivity {
    RecyclerView labTestRecyclerview;
    List<String> test_name_list;
    List<String> test_description_list;
    List<String> test_cost_list;
    SearchView search_test;
    labTestManagementAdapter labTestManagementAdapter;
    String search_HINT_color = "#434242";
    String search_color = "#434242";
    ImageView all_test;
    ImageView blood_test;
    ImageView cardio_test;
    ImageView liver_test;
    ImageView kidneys_test;
    ImageView lungs_test;
    View snack_bar_layout;
    HelperFunctions helperFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management_options);


        labTestRecyclerview = findViewById(R.id.searchLabTestRecycler);
        all_test = findViewById(R.id.all_test);
        blood_test = findViewById(R.id.blood_category);
        cardio_test = findViewById(R.id.cardio_category);
        liver_test = findViewById(R.id.liver_category);
        kidneys_test = findViewById(R.id.kidney_category);
        lungs_test = findViewById(R.id.lungs_category);
        search_test = findViewById(R.id.search_lab_test);


        snack_bar_layout = findViewById(android.R.id.content);
        helperFunctions = new HelperFunctions();



        int id = search_test.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = search_test.findViewById(id);
        textView.setTextColor(Color.parseColor(search_color));
        textView.setTextSize(14);
        textView.setHintTextColor(Color.parseColor(search_HINT_color));
        Typeface tf = ResourcesCompat.getFont(this,R.font.pt_sans_regular);
        textView.setTypeface(tf);

        test_name_list = new ArrayList<>();
        test_description_list = new ArrayList<>();
        test_cost_list = new ArrayList<>();


        all_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int all_count = 0;

                if(all_count == 0){
                    helperFunctions.snackBarShow(snack_bar_layout,"All tests selected");

                    test_name_list.clear();
                    test_description_list.clear();
                    test_cost_list.clear();

                    test_name_list.add("Complete Blood Count");
                    test_description_list.add("Known As: CBC, Blood For Analysis, Blood CP, Blood Complete Picture, Blood Routine Examination,Blood C/E Complete");
                    test_cost_list.add("550");

                    test_name_list.add("T-Spot TB");
                    test_description_list.add("Known As: Single Visit Tuberculosis,Blood Test for TB,Blood Test for Tuberculosis,IGRA,Interferon Gamma Release Assay");
                    test_cost_list.add("5,000");

                    test_name_list.add("CVM by PCR");
                    test_description_list.add("Known As: CMV Blood Test,CVM PCR,Cytomegalovirus Antibody");
                    test_cost_list.add("12,800");

                    test_name_list.add("Cytomegalovirus Antibody");
                    test_description_list.add("Known As: CMV Blood Test, IgG Test ");
                    test_cost_list.add("2,200");

                    test_name_list.add("Cytomegalovirus");
                    test_description_list.add("Known As: CMV Blood Test,Cytomegalovirus Antibody IgG");
                    test_cost_list.add("1,450");

                    test_name_list.add("Heart Disease Profile");
                    test_description_list.add("Known As: HDF, disease check out in heart");
                    test_cost_list.add("4,850");

                    test_name_list.add("Healthy Heart Profile");
                    test_description_list.add("Known As: HHF, to check out the health of heart");
                    test_cost_list.add("4,600");

                    test_name_list.add("Cardiovascular Risk Profile");
                    test_description_list.add("Known As: Cardiac Risk");
                    test_cost_list.add("3,500");

                    test_name_list.add("Cardiovascular Risk Profile");
                    test_description_list.add("Known As: Cardiac Risk");
                    test_cost_list.add("3,500");

                    test_name_list.add("Echocardiography");
                    test_description_list.add("Known As: Echocardiogram,Cardiac Echo");
                    test_cost_list.add("4,000");

                    test_name_list.add("Liver Function Test");
                    test_description_list.add("Known As: LFT,Liver Profile,Liver Panel");
                    test_cost_list.add("1,550");

                    test_name_list.add("Anti-LKM Antibodies");
                    test_description_list.add("Known As: Liver Kidney Microsomal Antibodies,LKM Antibodies");
                    test_cost_list.add("2,250");

                    test_name_list.add("MRI Liver Dynamic");
                    test_description_list.add("Known As: Magnetic Resonance Imaging Liver Dynamic ");
                    test_cost_list.add("17,000");

                    test_name_list.add("CT Biphasic for Liver");
                    test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
                    test_cost_list.add("29,000");

                    test_name_list.add("CT Biphasic for Liver");
                    test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
                    test_cost_list.add("29,000");

                    bloodTests(test_name_list,test_description_list,test_cost_list);
                }

            }
        });

        blood_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFunctions.snackBarShow(snack_bar_layout,"Blood tests selected");

                    test_name_list.clear();
                    test_description_list.clear();
                    test_cost_list.clear();

                    test_name_list.add("Complete Blood Count");
                    test_description_list.add("Known As: CBC, Blood For Analysis, Blood CP, Blood Complete Picture, Blood Routine Examination,Blood C/E Complete");
                    test_cost_list.add("550");

                    test_name_list.add("T-Spot TB");
                    test_description_list.add("Known As: Single Visit Tuberculosis,Blood Test for TB,Blood Test for Tuberculosis,IGRA,Interferon Gamma Release Assay");
                    test_cost_list.add("5,000");

                    test_name_list.add("CVM by PCR");
                    test_description_list.add("Known As: CMV Blood Test,CVM PCR,Cytomegalovirus Antibody");
                    test_cost_list.add("12,800");

                    bloodTests(test_name_list,test_description_list,test_cost_list);


            }
        });

        cardio_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFunctions.snackBarShow(snack_bar_layout,"Cardio tests selected");

                test_name_list.clear();
                test_description_list.clear();
                test_cost_list.clear();

                test_name_list.add("Heart Disease Profile");
                test_description_list.add("Known As: HDF, disease check out in heart");
                test_cost_list.add("4,850");

                test_name_list.add("Healthy Heart Profile");
                test_description_list.add("Known As: HHF, to check out the health of heart");
                test_cost_list.add("4,600");

                test_name_list.add("Cardiovascular Risk Profile");
                test_description_list.add("Known As: Cardiac Risk");
                test_cost_list.add("3,500");

                test_name_list.add("Cardiovascular Risk Profile");
                test_description_list.add("Known As: Cardiac Risk");
                test_cost_list.add("3,500");

                test_name_list.add("Echocardiography");
                test_description_list.add("Known As: Echocardiogram,Cardiac Echo");
                test_cost_list.add("4,000");

                bloodTests(test_name_list,test_description_list,test_cost_list);
            }
        });

        liver_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFunctions.snackBarShow(snack_bar_layout,"Liver tests selected");

                test_name_list.clear();
                test_description_list.clear();
                test_cost_list.clear();

                test_name_list.add("Liver Function Test");
                test_description_list.add("Known As: LFT,Liver Profile,Liver Panel");
                test_cost_list.add("1,550");

                test_name_list.add("Anti-LKM Antibodies");
                test_description_list.add("Known As: Liver Kidney Microsomal Antibodies,LKM Antibodies");
                test_cost_list.add("2,250");

                test_name_list.add("MRI Liver Dynamic");
                test_description_list.add("Known As: Magnetic Resonance Imaging Liver Dynamic ");
                test_cost_list.add("17,000");

                test_name_list.add("CT Biphasic for Liver");
                test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
                test_cost_list.add("29,000");

                test_name_list.add("CT Biphasic for Liver");
                test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
                test_cost_list.add("29,000");

                bloodTests(test_name_list,test_description_list,test_cost_list);
            }
        });

        kidneys_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFunctions.snackBarShow(snack_bar_layout,"Kidney tests selected");

                test_name_list.clear();
                test_description_list.clear();
                test_cost_list.clear();

                test_name_list.add("BIOPSY");
                test_description_list.add("Known As:BIOPSY (Kidney / Nephrostomy)");
                test_cost_list.add("5,450");

                test_name_list.add("ANTI LKM");
                test_description_list.add("Known As:ANTI LKM (Liver Kidney Microsomal) Ab. use to deteect the autoantibodies infected with any chronic disease");
                test_cost_list.add("2,240");


                test_name_list.add("Kidney Ultrasound");
                test_description_list.add("Known As:Ultrasound Kidney, Ureter, Bladder, an image is produced that ells us about the size, location of kidney");
                test_cost_list.add("5,450");


                test_name_list.add("Kidney Stone Detection");
                test_description_list.add("Known As:Kidney Stone For C/E , use to diagnose the presence of stones in kidney");
                test_cost_list.add("2,200");


                test_name_list.add("Renal Doppler");
                test_description_list.add("Known As:Renal Doppler Transplant Kidney, use to check the blood flow of kidney");
                test_cost_list.add("5,000");

                bloodTests(test_name_list,test_description_list,test_cost_list);


            }
        });

        lungs_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFunctions.snackBarShow(snack_bar_layout,"Lungs tests selected");
                test_name_list.clear();
                test_description_list.clear();
                test_cost_list.clear();

                test_name_list.add("BIOPSY");
                test_description_list.add("Known as:BIOPSY (Lobectomy of Lungs) Extra Large, use to remove detect the lobe in lungs");
                test_cost_list.add("5,450");

                test_name_list.add("Bronchoscopy");
                test_description_list.add("Known As:Bronchoscopy for air passage Ab. use to check the flow of air between the lungs");
                test_cost_list.add("1,550");


                test_name_list.add("Chest TP");
                test_description_list.add("Known As:Chest Tube Procedure,use to drain out the fluid or air from the lungs");
                test_cost_list.add("2,000");


                test_name_list.add("EBV");
                test_description_list.add("Known As: Endobronchial Valve , use to diagnose the hyperinflation between the lungs");
                test_cost_list.add("2,300");


                test_name_list.add("LFT");
                test_description_list.add("Known As:Lung Function Test, use to check how the lungs work");
                test_cost_list.add("1,200");

                bloodTests(test_name_list,test_description_list,test_cost_list);


            }
        });


        search_test.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                labTestManagementAdapter.getFilter().filter(newText);
                return true;

            }
        });

        allTestsShow();


    }

    private void bloodTests(List<String> test_name_dataset,List<String> test_desc_dataset,List<String> test_price_dataset){
        labTestRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        labTestManagementAdapter = new labTestManagementAdapter(test_name_list, test_description_list, test_cost_list);
        labTestRecyclerview.setAdapter(labTestManagementAdapter);
    }

    private void allTestsShow(){

        test_name_list.add("Complete Blood Count");
        test_description_list.add("Known As: CBC, Blood For Analysis, Blood CP, Blood Complete Picture, Blood Routine Examination,Blood C/E Complete");
        test_cost_list.add("550");

        test_name_list.add("T-Spot TB");
        test_description_list.add("Known As: Single Visit Tuberculosis,Blood Test for TB,Blood Test for Tuberculosis,IGRA,Interferon Gamma Release Assay");
        test_cost_list.add("5,000");

        test_name_list.add("CVM by PCR");
        test_description_list.add("Known As: CMV Blood Test,CVM PCR,Cytomegalovirus Antibody");
        test_cost_list.add("12,800");

        test_name_list.add("Cytomegalovirus Antibody");
        test_description_list.add("Known As: CMV Blood Test, IgG Test ");
        test_cost_list.add("2,200");

        test_name_list.add("Cytomegalovirus");
        test_description_list.add("Known As: CMV Blood Test,Cytomegalovirus Antibody IgG");
        test_cost_list.add("1,450");

        test_name_list.add("Heart Disease Profile");
        test_description_list.add("Known As: HDF, disease check out in heart");
        test_cost_list.add("4,850");

        test_name_list.add("Healthy Heart Profile");
        test_description_list.add("Known As: HHF, to check out the health of heart");
        test_cost_list.add("4,600");

        test_name_list.add("Cardiovascular Risk Profile");
        test_description_list.add("Known As: Cardiac Risk");
        test_cost_list.add("3,500");

        test_name_list.add("Cardiovascular Risk Profile");
        test_description_list.add("Known As: Cardiac Risk");
        test_cost_list.add("3,500");

        test_name_list.add("Echocardiography");
        test_description_list.add("Known As: Echocardiogram,Cardiac Echo");
        test_cost_list.add("4,000");

        test_name_list.add("Liver Function Test");
        test_description_list.add("Known As: LFT,Liver Profile,Liver Panel");
        test_cost_list.add("1,550");

        test_name_list.add("Anti-LKM Antibodies");
        test_description_list.add("Known As: Liver Kidney Microsomal Antibodies,LKM Antibodies");
        test_cost_list.add("2,250");

        test_name_list.add("MRI Liver Dynamic");
        test_description_list.add("Known As: Magnetic Resonance Imaging Liver Dynamic ");
        test_cost_list.add("17,000");

        test_name_list.add("CT Biphasic for Liver");
        test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
        test_cost_list.add("29,000");

        test_name_list.add("CT Biphasic for Liver");
        test_description_list.add("Known As:CT Biphasic for Liver, represents a considerable improvement in the detection of vascular liver neoplasms ");
        test_cost_list.add("29,000");

        test_name_list.add("BIOPSY");
        test_description_list.add("Known As:BIOPSY (Kidney / Nephrostomy)");
        test_cost_list.add("5,450");

        test_name_list.add("ANTI LKM");
        test_description_list.add("Known As:ANTI LKM (Liver Kidney Microsomal) Ab. use to deteect the autoantibodies infected with any chronic disease");
        test_cost_list.add("2,240");


        test_name_list.add("Kidney Ultrasound");
        test_description_list.add("Known As:Ultrasound Kidney, Ureter, Bladder, an image is produced that ells us about the size, location of kidney");
        test_cost_list.add("5,450");


        test_name_list.add("Kidney Stone Detection");
        test_description_list.add("Known As:Kidney Stone For C/E , use to diagnose the presence of stones in kidney");
        test_cost_list.add("2,200");


        test_name_list.add("Renal Doppler");
        test_description_list.add("Known As:Renal Doppler Transplant Kidney, use to check the blood flow of kidney");
        test_cost_list.add("5,000");

        test_name_list.add("BIOPSY");
        test_description_list.add("Known as:BIOPSY (Lobectomy of Lungs) Extra Large, use to remove detect the lobe in lungs");
        test_cost_list.add("5,450");

        test_name_list.add("Bronchoscopy");
        test_description_list.add("Known As:Bronchoscopy for air passage Ab. use to check the flow of air between the lungs");
        test_cost_list.add("1,550");


        test_name_list.add("Chest TP");
        test_description_list.add("Known As:Chest Tube Procedure,use to drain out the fluid or air from the lungs");
        test_cost_list.add("2,000");


        test_name_list.add("EBV");
        test_description_list.add("Known As: Endobronchial Valve , use to diagnose the hyperinflation between the lungs");
        test_cost_list.add("2,300");


        test_name_list.add("LFT");
        test_description_list.add("Known As:Lung Function Test, use to check how the lungs work");
        test_cost_list.add("1,200");

        labTestRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        labTestManagementAdapter = new labTestManagementAdapter(test_name_list, test_description_list, test_cost_list);
        labTestRecyclerview.setAdapter(labTestManagementAdapter);
    }
}


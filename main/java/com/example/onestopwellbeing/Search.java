package com.example.onestopwellbeing;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileAdapt adapter;
    private List<Profile> profileList;
    private FirebaseFirestore firestore;
    private Map<String, Integer> searchOptionImageMap;
    private static final int SPEECH_REQUEST_CODE = 123;
    private static final int DEFAULT_IMAGE_RESOURCE_ID = R.drawable.deep_yoga_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        profileList = new ArrayList<>();
        adapter = new ProfileAdapt(this, profileList);
        recyclerView.setAdapter(adapter);

        searchOptionImageMap = new HashMap<>();
        searchOptionImageMap.put("deep yoga", R.drawable.deep_yoga_image);
        searchOptionImageMap.put("mental health services", R.drawable.mental_health_image1);
        searchOptionImageMap.put("psychotherapy", R.drawable.psychotherapy_image);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void searchProfiles(String searchText, int imageResId) {
        if (TextUtils.isEmpty(searchText)) {
            profileList.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        firestore.collection("profiles")
                .whereEqualTo("name", searchText)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        profileList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Profile profile = documentSnapshot.toObject(Profile.class);
                            profile.setImageResId(imageResId);
                            profileList.add(profile);

                            if (searchText.equals("deep yoga")) {
                                Profile deepYogaProfile = new Profile();
                                deepYogaProfile.setName("Deep Yoga, Nottingham");
                                deepYogaProfile.setLocation("Nottingham's premier Yoga Studio and Yoga School");
                                deepYogaProfile.setImageResId(R.drawable.deep_yoga_image); // Ensure you have an appropriate image in your drawable resources
                                deepYogaProfile.setShowExtraInfo(true);
                                deepYogaProfile.setExtraInfo("Welcome to Deep Yoga, Nottingham. Please join us in Nottingham's premier Yoga Studio and Yoga School. Equipped with the best mats and props available (Liforme, Manduka, Alo, Lululemon, Warrior Addict, Firetoys) to enhance your experience. A space where our humble, but talented 😊 teachers look forward to helping you DISCOVER, EXPLORE and DEVELOP your yoga practice.");
                                profileList.add(0, deepYogaProfile);

                                Profile hotYogaProfile = new Profile();
                                hotYogaProfile.setName("Hot Yoga Nottingham");
                                hotYogaProfile.setLocation("70 N Sherwood St, Nottingham NG1 4EE");
                                hotYogaProfile.setImageResId(R.drawable.hot_yoga_nottingham);
                                hotYogaProfile.setShowExtraInfo(true);
                                profileList.add(hotYogaProfile);
                            } else if (searchText.equals("psychotherapy")) {
                                profileList.add(profile);
                                profileList.add(getNottsCounsellingAndPsychotherapyProfile());

                                Profile counsellingServiceProfile = new Profile();
                                counsellingServiceProfile.setName("Nottingham Counselling Service");
                                counsellingServiceProfile.setLocation("Unit 5, Victoria Court, Kent St, Nottingham NG1 3LZ");
                                counsellingServiceProfile.setImageResId(R.drawable.nottinghamcounselling_service);
                                counsellingServiceProfile.setShowExtraInfo(true);
                                counsellingServiceProfile.setExtraInfo("At Nottingham Counselling Service we believe that good mental wellbeing should not be restricted by circumstance or background.");
                                profileList.add(counsellingServiceProfile);

                            } else if (searchText.equalsIgnoreCase("mental health services")) {
                                Profile crisisSanctuariesProfile = new Profile();
                                crisisSanctuariesProfile.setName("Nottinghamshire Crisis Sanctuaries (Hounds Gate)");
                                crisisSanctuariesProfile.setLocation("73 Hounds Gate, Nottingham NG1 6BB");
                                crisisSanctuariesProfile.setImageResId(R.drawable.mental_health_crisis);
                                crisisSanctuariesProfile.setShowExtraInfo(true);
                                profileList.add(crisisSanctuariesProfile);

                                Profile marlowHouseProfile = new Profile();
                                marlowHouseProfile.setName("Marlow House");
                                marlowHouseProfile.setLocation("Marlow House, Waterford St, Old Basford, Nottingham NG6 0DH");
                                marlowHouseProfile.setImageResId(R.drawable.marlow_house_image);
                                marlowHouseProfile.setShowExtraInfo(true);
                                StringBuilder extraInfo = new StringBuilder();
                                extraInfo.append("Our 11 Local Mental Health Teams provide mental health services for people aged 18 to 65 years across Nottingham City, Nottinghamshire County, and Bassetlaw. The specialist teams include:");
                                extraInfo.append("\n\u2022 mental health nurses");
                                extraInfo.append("\n\u2022 occupational therapists");
                                extraInfo.append("\n\u2022 psychologists");
                                extraInfo.append("\n\u2022 psychiatrists");
                                extraInfo.append("\n\u2022 community support workers");
                                extraInfo.append("\n\u2022 peer support workers");
                                extraInfo.append("\n\u2022 employment specialists");
                                marlowHouseProfile.setExtraInfo(extraInfo.toString());
                                profileList.add(marlowHouseProfile);

                            } else if (searchText.equalsIgnoreCase("physiotherapy")) {
                                Profile physiotherapyProfile = new Profile();
                                physiotherapyProfile.setName("Physiotherapy Clinic");
                                physiotherapyProfile.setLocation("Tattershall Dr, Nottingham NG7 1BX");
                                physiotherapyProfile.setImageResId(R.drawable.physiotherapy);
                                physiotherapyProfile.setShowExtraInfo(true);
                                physiotherapyProfile.setExtraInfo("Nottingham’s highest rated 5 Gold Star physiotherapy clinic. Driven by a deep fascination with the human body and how it moves, our vision is to give everyone, everywhere the skills to maintain their mobility. That means having a presence at the heart of every community and offering a unique life-changing journey that enables you to control your own mobility.\n\nOur highly professional team is always on your side, going the extra mile to ensure you have the knowledge and understanding to independently and brilliantly care for your body and achieve the pain-free life you deserve.");
                                profileList.add(physiotherapyProfile);
                            } else {
                                profileList.add(profile);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Search.this, "Failed to search for profiles: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static Profile getNottsCounsellingAndPsychotherapyProfile() {
        Profile nottsProfile = new Profile();
        nottsProfile.setName("Notts Counselling & Psychotherapy");
        nottsProfile.setLocation("17 Regent St, Nottingham NG1 5BS");
        nottsProfile.setImageResId(R.drawable.nottingham_counselling_psychotherapy);
        nottsProfile.setShowExtraInfo(true);
        nottsProfile.setExtraInfo("I work with individuals, couples and organisations in a number of different ways. In all cases I offer an initial assessment discussion. I offer short-term contracts of up to 6 sessions around specific problem areas. I also work longer-term depending on the client and the complexity of the issues they bring.");
        return nottsProfile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    searchProfiles(query.trim(), DEFAULT_IMAGE_RESOURCE_ID);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    searchProfiles(newText.trim(), DEFAULT_IMAGE_RESOURCE_ID);
                    return true;
                }
                return false;
            }
        });

        MenuItem voiceMenuItem = menu.findItem(R.id.action_voice_search);
        voiceMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startVoiceRecognition();
                return true;
            }
        });

        return true;
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    String query = result.get(0);
                    if (query != null && !query.isEmpty()) {
                        String lowercaseQuery = query.toLowerCase();
                        for (String searchOption : searchOptionImageMap.keySet()) {
                            String lowercaseSearchOption = searchOption.toLowerCase();
                            if (lowercaseQuery.contains(lowercaseSearchOption)) {
                                searchProfiles(searchOption, searchOptionImageMap.get(searchOption));
                                return;
                            }
                        }
                        searchProfiles(query.trim(), DEFAULT_IMAGE_RESOURCE_ID);
                    }
                }
            } else {
                Toast.makeText(this, "Speech recognition failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
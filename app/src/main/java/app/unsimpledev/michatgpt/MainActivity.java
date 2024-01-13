package app.unsimpledev.michatgpt;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.speech.RecognizerIntent;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements SpeechRecognizer.Listener,  TextToSpeech.OnInitListener{

    private Gpt3Api gpt3Api;
    private EditText editTextQuestion;
    private LinearLayout chatLayout;
    private ProgressBar progressBar;
    private static final int REQUEST_CODE = 1;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private boolean isVoiceEnabled;

    private static final String MESSAGE_TYPE_RESPONSE = "RESPONSE";
    private static final String MESSAGE_TYPE_REQUEST = "REQUEST";
    private String longLanguage1 = "English";
    private String languageRegion1 = "en-US";
    private String language1 = "en";
    private String region1 = "USA";
    private String longLanguage2 = "Spanish";
    private String languageRegion2 = "es-ESP";
    private String language2 = "es";
    private String region2 = "ESP";
    private boolean isLeftSpeaking = true; // Initial alignment
    private boolean translateToRightLanguage = true;


    private ActivityResultLauncher<Intent> voiceRecognitionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // Handle the result here
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Process the recognized speech data (matches)
                }
            }
    );

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("GPT Translator");

        languageSpinner = findViewById(R.id.languageSpinner);

        languageSpinner2 = findViewById(R.id.languageSpinner2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        languageSpinner.setAdapter(adapter);

        languageSpinner2.setAdapter(adapter);

        // Preselect "English" in the Spinner
        languageSpinner.setSelection(Arrays.asList(languages).indexOf("English"));

        languageSpinner2.setSelection(Arrays.asList(languages).indexOf("Spanish"));

        // Set up listener to capture selected language
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected language
                longLanguage1 = languages[position];

                if (longLanguage1.equals("Afrikaans")) {
                    languageRegion1 = "af-ZA";
                    language1 = "af";
                    region1 = "ZA";
                } else if (longLanguage1.equals("Arabic")) {
                    languageRegion1 = "ar-SA";
                    language1 = "ar";
                    region1 = "SA";
                } else if (longLanguage1.equals("Armenian")) {
                    languageRegion1 = "hy-AM";
                    language1 = "hy";
                    region1 = "AM";
                } else if (longLanguage1.equals("Azerbaijani")) {
                    languageRegion1 = "az-AZ";
                    language1 = "az";
                    region1 = "AZ";
                } else if (longLanguage1.equals("Belarusian")) {
                    languageRegion1 = "be-BY";
                    language1 = "be";
                    region1 = "BY";
                } else if (longLanguage1.equals("Bosnian")) {
                    languageRegion1 = "bs-BA";
                    language1 = "bs";
                    region1 = "BA";
                } else if (longLanguage1.equals("Bulgarian")) {
                    languageRegion1 = "bg-BG";
                    language1 = "bg";
                    region1 = "BG";
                } else if (longLanguage1.equals("Catalan")) {
                    languageRegion1 = "ca-ES";
                    language1 = "ca";
                    region1 = "ES";
                } else if (longLanguage1.equals("Chinese")) {
                    languageRegion1 = "zh-CN";
                    language1 = "zh";
                    region1 = "CN";
                } else if (longLanguage1.equals("Croatian")) {
                    languageRegion1 = "hr-HR";
                    language1 = "hr";
                    region1 = "HR";
                } else if (longLanguage1.equals("Czech")) {
                    languageRegion1 = "cs-CZ";
                    language1 = "cs";
                    region1 = "CZ";
                } else if (longLanguage1.equals("Danish")) {
                    languageRegion1 = "da-DK";
                    language1 = "da";
                    region1 = "DK";
                } else if (longLanguage1.equals("Dutch")) {
                    languageRegion1 = "nl-NL";
                    language1 = "nl";
                    region1 = "NL";
                } else if (longLanguage1.equals("English")) {
                    languageRegion1 = "en-US";
                    language1 = "en";
                    region1 = "USA";
                } else if (longLanguage1.equals("Estonian")) {
                    languageRegion1 = "et-EE";
                    language1 = "et";
                    region1 = "EE";
                } else if (longLanguage1.equals("Finnish")) {
                    languageRegion1 = "fi-FI";
                    language1 = "fi";
                    region1 = "FI";
                } else if (longLanguage1.equals("French")) {
                    languageRegion1 = "fr-FR";
                    language1 = "fr";
                    region1 = "FR";
                } else if (longLanguage1.equals("Galician")) {
                    languageRegion1 = "gl-ES";
                    language1 = "gl";
                    region1 = "ES";
                } else if (longLanguage1.equals("German")) {
                    languageRegion1 = "de-DE";
                    language1 = "de";
                    region1 = "DE";
                } else if (longLanguage1.equals("Greek")) {
                    languageRegion1 = "el-GR";
                    language1 = "el";
                    region1 = "GR";
                } else if (longLanguage1.equals("Hebrew")) {
                    languageRegion1 = "he-IL";
                    language1 = "he";
                    region1 = "IL";
                } else if (longLanguage1.equals("Hindi")) {
                    languageRegion1 = "hi-IN";
                    language1 = "hi";
                    region1 = "IN";
                } else if (longLanguage1.equals("Hungarian")) {
                    languageRegion1 = "hu-HU";
                    language1 = "hu";
                    region1 = "HU";
                } else if (longLanguage1.equals("Icelandic")) {
                    languageRegion1 = "is-IS";
                    language1 = "is";
                    region1 = "IS";
                } else if (longLanguage1.equals("Indonesian")) {
                    languageRegion1 = "id-ID";
                    language1 = "id";
                    region1 = "ID";
                } else if (longLanguage1.equals("Italian")) {
                    languageRegion1 = "it-IT";
                    language1 = "it";
                    region1 = "IT";
                } else if (longLanguage1.equals("Japanese")) {
                    languageRegion1 = "ja-JP";
                    language1 = "ja";
                    region1 = "JP";
                } else if (longLanguage1.equals("Kannada")) {
                    languageRegion1 = "kn-IN";
                    language1 = "kn";
                    region1 = "IN";
                } else if (longLanguage1.equals("Kazakh")) {
                    languageRegion1 = "kk-KZ";
                    language1 = "kk";
                    region1 = "KZ";
                } else if (longLanguage1.equals("Korean")) {
                    languageRegion1 = "ko-KR";
                    language1 = "ko";
                    region1 = "KR";
                } else if (longLanguage1.equals("Latvian")) {
                    languageRegion1 = "lv-LV";
                    language1 = "lv";
                    region1 = "LV";
                } else if (longLanguage1.equals("Lithuanian")) {
                    languageRegion1 = "lt-LT";
                    language1 = "lt";
                    region1 = "LT";
                } else if (longLanguage1.equals("Macedonian")) {
                    languageRegion1 = "mk-MK";
                    language1 = "mk";
                    region1 = "MK";
                } else if (longLanguage1.equals("Malay")) {
                    languageRegion1 = "ms-MY";
                    language1 = "ms";
                    region1 = "MY";
                } else if (longLanguage1.equals("Marathi")) {
                    languageRegion1 = "mr-IN";
                    language1 = "mr";
                    region1 = "IN";
                } else if (longLanguage1.equals("Maori")) {
                    languageRegion1 = "mi-NZ";
                    language1 = "mi";
                    region1 = "NZ";
                } else if (longLanguage1.equals("Nepali")) {
                    languageRegion1 = "ne-NP";
                    language1 = "ne";
                    region1 = "NP";
                } else if (longLanguage1.equals("Norwegian")) {
                    languageRegion1 = "no-NO";
                    language1 = "no";
                    region1 = "NO";
                } else if (longLanguage1.equals("Persian")) {
                    languageRegion1 = "fa-IR";
                    language1 = "fa";
                    region1 = "IR";
                } else if (longLanguage1.equals("Polish")) {
                    languageRegion1 = "pl-PL";
                    language1 = "pl";
                    region1 = "PL";
                } else if (longLanguage1.equals("Portuguese")) {
                    languageRegion1 = "pt-PT";
                    language1 = "pt";
                    region1 = "PT";
                } else if (longLanguage1.equals("Romanian")) {
                    languageRegion1 = "ro-RO";
                    language1 = "ro";
                    region1 = "RO";
                } else if (longLanguage1.equals("Russian")) {
                    languageRegion1 = "ru-RU";
                    language1 = "ru";
                    region1 = "RU";
                } else if (longLanguage1.equals("Serbian")) {
                    languageRegion1 = "sr-RS";
                    language1 = "sr";
                    region1 = "RS";
                } else if (longLanguage1.equals("Slovak")) {
                    languageRegion1 = "sk-SK";
                    language1 = "sk";
                    region1 = "SK";
                } else if (longLanguage1.equals("Slovenian")) {
                    languageRegion1 = "sl-SI";
                    language1 = "sl";
                    region1 = "SI";
                } else if (longLanguage1.equals("Spanish")) {
                    languageRegion1 = "es-ES";
                    language1 = "es";
                    region1 = "ESP";
                } else if (longLanguage1.equals("Swahili")) {
                    languageRegion1 = "sw-TZ";
                    language1 = "sw";
                    region1 = "TZ";
                } else if (longLanguage1.equals("Swedish")) {
                    languageRegion1 = "sv-SE";
                    language1 = "sv";
                    region1 = "SE";
                } else if (longLanguage1.equals("Tagalog")) {
                    languageRegion1 = "tl-PH";
                    language1 = "tl";
                    region1 = "PH";
                } else if (longLanguage1.equals("Tamil")) {
                    languageRegion1 = "ta-IN";
                    language1 = "ta";
                    region1 = "IN";
                } else if (longLanguage1.equals("Thai")) {
                    languageRegion1 = "th-TH";
                    language1 = "th";
                    region1 = "TH";
                } else if (longLanguage1.equals("Turkish")) {
                    languageRegion1 = "tr-TR";
                    language1 = "tr";
                    region1 = "TR";
                } else if (longLanguage1.equals("Ukrainian")) {
                    languageRegion1 = "uk-UA";
                    language1 = "uk";
                    region1 = "UA";
                } else if (longLanguage1.equals("Urdu")) {
                    languageRegion1 = "ur-PK";
                    language1 = "ur";
                    region1 = "PK";
                } else if (longLanguage1.equals("Vietnamese")) {
                    languageRegion1 = "vi-VN";
                    language1 = "vi";
                    region1 = "VN";
                } else if (longLanguage1.equals("Welsh")) {
                    languageRegion1 = "cy-GB";
                    language1 = "cy";
                    region1 = "GBR";
                }

                // You can use the selectedLanguage variable here or pass it to another method
                // For example, log the selected language
                Log.d("SelectedLanguage", "Selected Language: " + longLanguage1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected if needed
            }
        });

        // Set up listener to capture selected language
        languageSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected language
                longLanguage2 = languages[position];
                if (longLanguage2.equals("Afrikaans")) {
                    languageRegion2 = "af-ZA";
                    language2 = "af";
                    region2 = "ZA";
                } else if (longLanguage2.equals("Arabic")) {
                    languageRegion2 = "ar-SA";
                    language2 = "ar";
                    region2 = "SA";
                } else if (longLanguage2.equals("Armenian")) {
                    languageRegion2 = "hy-AM";
                    language2 = "hy";
                    region2 = "AM";
                } else if (longLanguage2.equals("Azerbaijani")) {
                    languageRegion2 = "az-AZ";
                    language2 = "az";
                    region2 = "AZ";
                } else if (longLanguage2.equals("Belarusian")) {
                    languageRegion2 = "be-BY";
                    language2 = "be";
                    region2 = "BY";
                } else if (longLanguage2.equals("Bosnian")) {
                    languageRegion2 = "bs-BA";
                    language2 = "bs";
                    region2 = "BA";
                } else if (longLanguage2.equals("Bulgarian")) {
                    languageRegion2 = "bg-BG";
                    language2 = "bg";
                    region2 = "BG";
                } else if (longLanguage2.equals("Catalan")) {
                    languageRegion2 = "ca-ES";
                    language2 = "ca";
                    region2 = "ES";
                } else if (longLanguage2.equals("Chinese")) {
                    languageRegion2 = "zh-CN";
                    language2 = "zh";
                    region2 = "CN";
                } else if (longLanguage2.equals("Croatian")) {
                    languageRegion2 = "hr-HR";
                    language2 = "hr";
                    region2 = "HR";
                } else if (longLanguage2.equals("Czech")) {
                    languageRegion2 = "cs-CZ";
                    language2 = "cs";
                    region2 = "CZ";
                } else if (longLanguage2.equals("Danish")) {
                    languageRegion2 = "da-DK";
                    language2 = "da";
                    region2 = "DK";
                } else if (longLanguage2.equals("Dutch")) {
                    languageRegion2 = "nl-NL";
                    language2 = "nl";
                    region2 = "NL";
                } else if (longLanguage2.equals("English")) {
                    languageRegion2 = "en-US";
                    language2 = "en";
                    region2 = "USA";
                } else if (longLanguage2.equals("Estonian")) {
                    languageRegion2 = "et-EE";
                    language2 = "et";
                    region2 = "EE";
                } else if (longLanguage2.equals("Finnish")) {
                    languageRegion2 = "fi-FI";
                    language2 = "fi";
                    region2 = "FI";
                } else if (longLanguage2.equals("French")) {
                    languageRegion2 = "fr-FR";
                    language2 = "fr";
                    region2 = "FR";
                } else if (longLanguage2.equals("Galician")) {
                    languageRegion2 = "gl-ES";
                    language2 = "gl";
                    region2 = "ES";
                } else if (longLanguage2.equals("German")) {
                    languageRegion2 = "de-DE";
                    language2 = "de";
                    region2 = "DE";
                } else if (longLanguage2.equals("Greek")) {
                    languageRegion2 = "el-GR";
                    language2 = "el";
                    region2 = "GR";
                } else if (longLanguage2.equals("Hebrew")) {
                    languageRegion2 = "he-IL";
                    language2 = "he";
                    region2 = "IL";
                } else if (longLanguage2.equals("Hindi")) {
                    languageRegion2 = "hi-IN";
                    language2 = "hi";
                    region2 = "IN";
                } else if (longLanguage2.equals("Hungarian")) {
                    languageRegion2 = "hu-HU";
                    language2 = "hu";
                    region2 = "HU";
                } else if (longLanguage2.equals("Icelandic")) {
                    languageRegion2 = "is-IS";
                    language2 = "is";
                    region2 = "IS";
                } else if (longLanguage2.equals("Indonesian")) {
                    languageRegion2 = "id-ID";
                    language2 = "id";
                    region2 = "ID";
                } else if (longLanguage2.equals("Italian")) {
                    languageRegion2 = "it-IT";
                    language2 = "it";
                    region2 = "IT";
                } else if (longLanguage2.equals("Japanese")) {
                    languageRegion2 = "ja-JP";
                    language2 = "ja";
                    region2 = "JP";
                } else if (longLanguage2.equals("Kannada")) {
                    languageRegion2 = "kn-IN";
                    language2 = "kn";
                    region2 = "IN";
                } else if (longLanguage2.equals("Kazakh")) {
                    languageRegion2 = "kk-KZ";
                    language2 = "kk";
                    region2 = "KZ";
                } else if (longLanguage2.equals("Korean")) {
                    languageRegion2 = "ko-KR";
                    language2 = "ko";
                    region2 = "KR";
                } else if (longLanguage2.equals("Latvian")) {
                    languageRegion2 = "lv-LV";
                    language2 = "lv";
                    region2 = "LV";
                } else if (longLanguage2.equals("Lithuanian")) {
                    languageRegion2 = "lt-LT";
                    language2 = "lt";
                    region2 = "LT";
                } else if (longLanguage2.equals("Macedonian")) {
                    languageRegion2 = "mk-MK";
                    language2 = "mk";
                    region2 = "MK";
                } else if (longLanguage2.equals("Malay")) {
                    languageRegion2 = "ms-MY";
                    language2 = "ms";
                    region2 = "MY";
                } else if (longLanguage2.equals("Marathi")) {
                    languageRegion2 = "mr-IN";
                    language2 = "mr";
                    region2 = "IN";
                } else if (longLanguage2.equals("Maori")) {
                    languageRegion2 = "mi-NZ";
                    language2 = "mi";
                    region2 = "NZ";
                } else if (longLanguage2.equals("Nepali")) {
                    languageRegion2 = "ne-NP";
                    language2 = "ne";
                    region2 = "NP";
                } else if (longLanguage2.equals("Norwegian")) {
                    languageRegion2 = "no-NO";
                    language2 = "no";
                    region2 = "NO";
                } else if (longLanguage2.equals("Persian")) {
                    languageRegion2 = "fa-IR";
                    language2 = "fa";
                    region2 = "IR";
                } else if (longLanguage2.equals("Polish")) {
                    languageRegion2 = "pl-PL";
                    language2 = "pl";
                    region2 = "PL";
                } else if (longLanguage2.equals("Portuguese")) {
                    languageRegion2 = "pt-PT";
                    language2 = "pt";
                    region2 = "PT";
                } else if (longLanguage2.equals("Romanian")) {
                    languageRegion2 = "ro-RO";
                    language2 = "ro";
                    region2 = "ROU";
                } else if (longLanguage2.equals("Russian")) {
                    languageRegion2 = "ru-RU";
                    language2 = "ru";
                    region2 = "RUS";
                } else if (longLanguage2.equals("Serbian")) {
                    languageRegion2 = "sr-RS";
                    language2 = "sr";
                    region2 = "RS";
                } else if (longLanguage2.equals("Slovak")) {
                    languageRegion2 = "sk-SK";
                    language2 = "sk";
                    region2 = "SK";
                } else if (longLanguage2.equals("Slovenian")) {
                    languageRegion2 = "sl-SI";
                    language2 = "sl";
                    region2 = "SI";
                } else if (longLanguage2.equals("Spanish")) {
                    languageRegion2 = "es-ES";
                    language2 = "es";
                    region2 = "ES";
                } else if (longLanguage2.equals("Swahili")) {
                    languageRegion2 = "sw-TZ";
                    language2 = "sw";
                    region2 = "TZ";
                } else if (longLanguage2.equals("Swedish")) {
                    languageRegion2 = "sv-SE";
                    language2 = "sv";
                    region2 = "SE";
                } else if (longLanguage2.equals("Tagalog")) {
                    languageRegion2 = "tl-PH";
                    language2 = "tl";
                    region2 = "PH";
                } else if (longLanguage2.equals("Tamil")) {
                    languageRegion2 = "ta-IN";
                    language2 = "ta";
                    region2 = "IN";
                } else if (longLanguage2.equals("Thai")) {
                    languageRegion2 = "th-TH";
                    language2 = "th";
                    region2 = "TH";
                } else if (longLanguage2.equals("Turkish")) {
                    languageRegion2 = "tr-TR";
                    language2 = "tr";
                    region2 = "TR";
                } else if (longLanguage2.equals("Ukrainian")) {
                    languageRegion2 = "uk-UA";
                    language2 = "uk";
                    region2 = "UA";
                } else if (longLanguage2.equals("Urdu")) {
                    languageRegion2 = "ur-PK";
                    language2 = "ur";
                    region2 = "PK";
                } else if (longLanguage2.equals("Vietnamese")) {
                    languageRegion2 = "vi-VN";
                    language2 = "vi";
                    region2 = "VN";
                } else if (longLanguage2.equals("Welsh")) {
                    languageRegion2 = "cy-GB";
                    language2 = "cy";
                    region2 = "GB";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected if needed
            }
        });

        gpt3Api = new Gpt3Api(this);
        chatLayout = findViewById(R.id.layoutInput);
        progressBar = findViewById(R.id.progressBar);

        speechRecognizer = new SpeechRecognizer(this, REQUEST_CODE, this);
        textToSpeech = new TextToSpeech(this, this);
        isVoiceEnabled = getSharedPreferences("MICHATGPT", MODE_PRIVATE).getBoolean("CHECKRESPVOICE", true);

        Button buttonSpeech = findViewById(R.id.buttonSpeech);
        Button buttonClear = findViewById(R.id.buttonClear);
        ImageView imageView = findViewById(R.id.imageView);
        final float[] currentRotation = {imageView.getRotation()};
        buttonSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newRotation = currentRotation[0] + 180;
                if(isLeftSpeaking)
                    speechRecognizer.start(languageRegion1);
                else
                    speechRecognizer.start(languageRegion2);

                RotateAnimation rotate = new RotateAnimation(currentRotation[0], newRotation,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500); // Duration of the animation in milliseconds
                rotate.setFillAfter(true); // Keeps the rotated state after animation

                currentRotation[0] = newRotation % 360;

                imageView.startAnimation(rotate);

            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset conversation if the arrow points to the left
                if (currentRotation[0] <= 180) {
                    RotateAnimation rotate = new RotateAnimation(currentRotation[0], 0,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(500); // Duration of the animation in milliseconds
                    rotate.setFillAfter(true); // Keeps the rotated state after animation

                    currentRotation[0] = 0;

                    imageView.startAnimation(rotate);

                    isLeftSpeaking = true;
                }
                // Remove all text from the chat
                chatLayout.removeAllViews();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float newRotation = currentRotation[0] + 180;

                RotateAnimation rotate = new RotateAnimation(currentRotation[0], newRotation,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500); // Duration of the animation in milliseconds
                rotate.setFillAfter(true); // Keeps the rotated state after animation

                currentRotation[0] = newRotation % 360;

                isLeftSpeaking = !isLeftSpeaking;

                imageView.startAnimation(rotate);

                // Set the final rotation after the animation completes
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

    }

    private void addChatMessage(String message, String type) {
        if (message.startsWith("\n\n")) {
            message = message.replaceFirst("\n\n", "");
        }
        else if (message.startsWith("\n")) {
            message = message.replaceFirst("\n", "");
        }
        if (MESSAGE_TYPE_RESPONSE.equals(type)){
            TextView textView = new TextView(this);
            textView.setText(message);
            textView.setTypeface(null, Typeface.BOLD_ITALIC);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 5, 30, 10);

            if (isLeftSpeaking) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                textView.setLayoutParams(params);
                chatLayout.addView(textView);
                callTextToSpeech(message, new Locale(language2, region2));

            } else {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                textView.setLayoutParams(params);
                chatLayout.addView(textView);
                callTextToSpeech(message, new Locale(language1, region1));
            }



        }
    }

    private void callChatGpt(String prompt){
        progressBar.setVisibility(View.VISIBLE);
        gpt3Api.generateText(prompt,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        addChatMessage(response, MESSAGE_TYPE_RESPONSE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        addChatMessage("Error: " + error.getMessage(), MESSAGE_TYPE_RESPONSE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        speechRecognizer.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSpeechRecognized(String text) {
        if (isLeftSpeaking)
            callChatGpt("Translate the following " + longLanguage1 + " text to " + longLanguage2 + ": " + text);
        else
            callChatGpt("Translate the following " + longLanguage2 + " text to " + longLanguage1 + ": " + text);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result;
            Locale languageLocale;
            if(isLeftSpeaking) {
                languageLocale = new Locale(language2, region2);
                result = textToSpeech.setLanguage(languageLocale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Lenguaje no soportado");
                }
            }
            else {
                languageLocale = new Locale(language1, region1);
                result = textToSpeech.setLanguage(languageLocale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Lenguaje no soportado");
                }
            }
        } else {
            Log.e("TextToSpeech", "Inicialización fallida");
        }
    }

    private void callTextToSpeech(String text, Locale languageLocale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.setLanguage(languageLocale);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.setLanguage(languageLocale);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        isLeftSpeaking = !isLeftSpeaking;
    }

    private Spinner languageSpinner;

    private Spinner languageSpinner2;
    private String[] languages = {
            "Afrikaans", "Arabic", "Armenian", "Azerbaijani", "Belarusian", "Bosnian", "Bulgarian", "Catalan", "Chinese",
            "Croatian", "Czech", "Danish", "Dutch", "English", "Estonian", "Finnish", "French", "Galician", "German",
            "Greek", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Italian", "Japanese", "Kannada", "Kazakh",
            "Korean", "Latvian", "Lithuanian", "Macedonian", "Malay", "Marathi", "Maori", "Nepali", "Norwegian", "Persian",
            "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish", "Swahili", "Swedish",
            "Tagalog", "Tamil", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Welsh"
    };

}
package app.unsimpledev.michatgpt;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    private String languageTranslated = "English"; // Variable to hold the selected language
    private String regionTranslated = "en-US"; // American english, for example
    private String languageToTranslate = "es"; // Variable to hold the selected language
    private String regionToTranslate = "ES"; // Variable to hold the selected language
    private String longLanguageToTranslate = "Spanish";


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                languageTranslated = languages[position];

                if (languageTranslated.equals("Afrikaans")) {
                    regionTranslated = "af-ZA";
                } else if (languageTranslated.equals("Arabic")) {
                    regionTranslated = "ar-SA";
                } else if (languageTranslated.equals("Armenian")) {
                    regionTranslated = "hy-AM";
                } else if (languageTranslated.equals("Azerbaijani")) {
                    regionTranslated = "az-AZ";
                } else if (languageTranslated.equals("Belarusian")) {
                    regionTranslated = "be-BY";
                } else if (languageTranslated.equals("Bosnian")) {
                    regionTranslated = "bs-BA";
                } else if (languageTranslated.equals("Bulgarian")) {
                    regionTranslated = "bg-BG";
                } else if (languageTranslated.equals("Catalan")) {
                    regionTranslated = "ca-ES";
                } else if (languageTranslated.equals("Chinese")) {
                    regionTranslated = "zh-CN";
                } else if (languageTranslated.equals("Croatian")) {
                    regionTranslated = "hr-HR";
                } else if (languageTranslated.equals("Czech")) {
                    regionTranslated = "cs-CZ";
                } else if (languageTranslated.equals("Danish")) {
                    regionTranslated = "da-DK";
                } else if (languageTranslated.equals("Dutch")) {
                    regionTranslated = "nl-NL";
                } else if (languageTranslated.equals("English")) {
                    regionTranslated = "en-US";
                } else if (languageTranslated.equals("Estonian")) {
                    regionTranslated = "et-EE";
                } else if (languageTranslated.equals("Finnish")) {
                    regionTranslated = "fi-FI";
                } else if (languageTranslated.equals("French")) {
                    regionTranslated = "fr-FR";
                } else if (languageTranslated.equals("Galician")) {
                    regionTranslated = "gl-ES";
                } else if (languageTranslated.equals("German")) {
                    regionTranslated = "de-DE";
                } else if (languageTranslated.equals("Greek")) {
                    regionTranslated = "el-GR";
                } else if (languageTranslated.equals("Hebrew")) {
                    regionTranslated = "he-IL";
                } else if (languageTranslated.equals("Hindi")) {
                    regionTranslated = "hi-IN";
                } else if (languageTranslated.equals("Hungarian")) {
                    regionTranslated = "hu-HU";
                } else if (languageTranslated.equals("Icelandic")) {
                    regionTranslated = "is-IS";
                } else if (languageTranslated.equals("Indonesian")) {
                    regionTranslated = "id-ID";
                } else if (languageTranslated.equals("Italian")) {
                    regionTranslated = "it-IT";
                } else if (languageTranslated.equals("Japanese")) {
                    regionTranslated = "ja-JP";
                } else if (languageTranslated.equals("Kannada")) {
                    regionTranslated = "kn-IN";
                } else if (languageTranslated.equals("Kazakh")) {
                    regionTranslated = "kk-KZ";
                } else if (languageTranslated.equals("Korean")) {
                    regionTranslated = "ko-KR";
                } else if (languageTranslated.equals("Latvian")) {
                    regionTranslated = "lv-LV";
                } else if (languageTranslated.equals("Lithuanian")) {
                    regionTranslated = "lt-LT";
                } else if (languageTranslated.equals("Macedonian")) {
                    regionTranslated = "mk-MK";
                } else if (languageTranslated.equals("Malay")) {
                    regionTranslated = "ms-MY";
                } else if (languageTranslated.equals("Marathi")) {
                    regionTranslated = "mr-IN";
                } else if (languageTranslated.equals("Maori")) {
                    regionTranslated = "mi-NZ";
                } else if (languageTranslated.equals("Nepali")) {
                    regionTranslated = "ne-NP";
                } else if (languageTranslated.equals("Norwegian")) {
                    regionTranslated = "no-NO";
                } else if (languageTranslated.equals("Persian")) {
                    regionTranslated = "fa-IR";
                } else if (languageTranslated.equals("Polish")) {
                    regionTranslated = "pl-PL";
                } else if (languageTranslated.equals("Portuguese")) {
                    regionTranslated = "pt-PT";
                } else if (languageTranslated.equals("Romanian")) {
                    regionTranslated = "ro-RO";
                } else if (languageTranslated.equals("Russian")) {
                    regionTranslated = "ru-RU";
                } else if (languageTranslated.equals("Serbian")) {
                    regionTranslated = "sr-RS";
                } else if (languageTranslated.equals("Slovak")) {
                    regionTranslated = "sk-SK";
                } else if (languageTranslated.equals("Slovenian")) {
                    regionTranslated = "sl-SI";
                } else if (languageTranslated.equals("Spanish")) {
                    regionTranslated = "es-ES";
                } else if (languageTranslated.equals("Swahili")) {
                    regionTranslated = "sw-TZ";
                } else if (languageTranslated.equals("Swedish")) {
                    regionTranslated = "sv-SE";
                } else if (languageTranslated.equals("Tagalog")) {
                    regionTranslated = "tl-PH";
                } else if (languageTranslated.equals("Tamil")) {
                    regionTranslated = "ta-IN";
                } else if (languageTranslated.equals("Thai")) {
                    regionTranslated = "th-TH";
                } else if (languageTranslated.equals("Turkish")) {
                    regionTranslated = "tr-TR";
                } else if (languageTranslated.equals("Ukrainian")) {
                    regionTranslated = "uk-UA";
                } else if (languageTranslated.equals("Urdu")) {
                    regionTranslated = "ur-PK";
                } else if (languageTranslated.equals("Vietnamese")) {
                    regionTranslated = "vi-VN";
                } else if (languageTranslated.equals("Welsh")) {
                    regionTranslated = "cy-GB";
                }

                // You can use the selectedLanguage variable here or pass it to another method
                // For example, log the selected language
                Log.d("SelectedLanguage", "Selected Language: " + languageTranslated);
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
                longLanguageToTranslate = languages[position];
                if (longLanguageToTranslate.equals("Afrikaans")) {
                    regionToTranslate = "ZA";
                    languageToTranslate = "af";
                } else if (longLanguageToTranslate.equals("Arabic")) {
                    regionToTranslate = "SA";
                    languageToTranslate = "ar";
                } else if (longLanguageToTranslate.equals("Armenian")) {
                    regionToTranslate = "AM";
                    languageToTranslate = "hy";
                } else if (longLanguageToTranslate.equals("Azerbaijani")) {
                    regionToTranslate = "AZ";
                    languageToTranslate = "az";
                } else if (longLanguageToTranslate.equals("Belarusian")) {
                    regionToTranslate = "BY";
                    languageToTranslate = "be";
                } else if (longLanguageToTranslate.equals("Bosnian")) {
                    regionToTranslate = "BA";
                    languageToTranslate = "bs";
                } else if (longLanguageToTranslate.equals("Bulgarian")) {
                    regionToTranslate = "BG";
                    languageToTranslate = "bg";
                } else if (longLanguageToTranslate.equals("Catalan")) {
                    regionToTranslate = "ES";
                    languageToTranslate = "ca";
                } else if (longLanguageToTranslate.equals("Chinese")) {
                    regionToTranslate = "CN";
                    languageToTranslate = "zh";
                } else if (longLanguageToTranslate.equals("Croatian")) {
                    regionToTranslate = "HR";
                    languageToTranslate = "hr";
                } else if (longLanguageToTranslate.equals("Czech")) {
                    regionToTranslate = "CZ";
                    languageToTranslate = "cs";
                } else if (longLanguageToTranslate.equals("Danish")) {
                    regionToTranslate = "DK";
                    languageToTranslate = "da";
                } else if (longLanguageToTranslate.equals("Dutch")) {
                    regionToTranslate = "NL";
                    languageToTranslate = "nl";
                } else if (longLanguageToTranslate.equals("English")) {
                    regionToTranslate = "US";
                    languageToTranslate = "en";
                } else if (longLanguageToTranslate.equals("Estonian")) {
                    regionToTranslate = "EE";
                    languageToTranslate = "et";
                } else if (longLanguageToTranslate.equals("Finnish")) {
                    regionToTranslate = "FI";
                    languageToTranslate = "fi";
                } else if (longLanguageToTranslate.equals("French")) {
                    regionToTranslate = "FR";
                    languageToTranslate = "fr";
                } else if (longLanguageToTranslate.equals("Galician")) {
                    regionToTranslate = "ES";
                    languageToTranslate = "gl";
                } else if (longLanguageToTranslate.equals("German")) {
                    regionToTranslate = "DE";
                    languageToTranslate = "de";
                } else if (longLanguageToTranslate.equals("Greek")) {
                    regionToTranslate = "GR";
                    languageToTranslate = "el";
                } else if (longLanguageToTranslate.equals("Hebrew")) {
                    regionToTranslate = "IL";
                    languageToTranslate = "he";
                } else if (longLanguageToTranslate.equals("Hindi")) {
                    regionToTranslate = "IN";
                    languageToTranslate = "hi";
                } else if (longLanguageToTranslate.equals("Hungarian")) {
                    regionToTranslate = "HU";
                    languageToTranslate = "hu";
                } else if (longLanguageToTranslate.equals("Icelandic")) {
                    regionToTranslate = "IS";
                    languageToTranslate = "is";
                } else if (longLanguageToTranslate.equals("Indonesian")) {
                    regionToTranslate = "ID";
                    languageToTranslate = "id";
                } else if (longLanguageToTranslate.equals("Italian")) {
                    regionToTranslate = "IT";
                    languageToTranslate = "it";
                } else if (longLanguageToTranslate.equals("Japanese")) {
                    regionToTranslate = "JP";
                    languageToTranslate = "ja";
                } else if (longLanguageToTranslate.equals("Kannada")) {
                    regionToTranslate = "IN";
                    languageToTranslate = "kn";
                } else if (longLanguageToTranslate.equals("Kazakh")) {
                    regionToTranslate = "KZ";
                    languageToTranslate = "kk";
                } else if (longLanguageToTranslate.equals("Korean")) {
                    regionToTranslate = "KR";
                    languageToTranslate = "ko";
                } else if (longLanguageToTranslate.equals("Latvian")) {
                    regionToTranslate = "LV";
                    languageToTranslate = "lv";
                } else if (longLanguageToTranslate.equals("Lithuanian")) {
                    regionToTranslate = "LT";
                    languageToTranslate = "lt";
                } else if (longLanguageToTranslate.equals("Macedonian")) {
                    regionToTranslate = "MK";
                    languageToTranslate = "mk";
                } else if (longLanguageToTranslate.equals("Malay")) {
                    regionToTranslate = "MY";
                    languageToTranslate = "ms";
                } else if (longLanguageToTranslate.equals("Marathi")) {
                    regionToTranslate = "IN";
                    languageToTranslate = "mr";
                } else if (longLanguageToTranslate.equals("Maori")) {
                    regionToTranslate = "NZ";
                    languageToTranslate = "mi";
                } else if (longLanguageToTranslate.equals("Nepali")) {
                    regionToTranslate = "NP";
                    languageToTranslate = "ne";
                } else if (longLanguageToTranslate.equals("Norwegian")) {
                    regionToTranslate = "NO";
                    languageToTranslate = "no";
                } else if (longLanguageToTranslate.equals("Persian")) {
                    regionToTranslate = "IR";
                    languageToTranslate = "fa";
                } else if (longLanguageToTranslate.equals("Polish")) {
                    regionToTranslate = "PL";
                    languageToTranslate = "pl";
                } else if (longLanguageToTranslate.equals("Portuguese")) {
                    regionToTranslate = "PT";
                    languageToTranslate = "pt";
                } else if (longLanguageToTranslate.equals("Romanian")) {
                    regionToTranslate = "RO";
                    languageToTranslate = "ro";
                } else if (longLanguageToTranslate.equals("Russian")) {
                    regionToTranslate = "RU";
                    languageToTranslate = "ru";
                } else if (longLanguageToTranslate.equals("Serbian")) {
                    regionToTranslate = "RS";
                    languageToTranslate = "sr";
                } else if (longLanguageToTranslate.equals("Slovak")) {
                    regionToTranslate = "SK";
                    languageToTranslate = "sk";
                } else if (longLanguageToTranslate.equals("Slovenian")) {
                    regionToTranslate = "SI";
                    languageToTranslate = "sl";
                } else if (longLanguageToTranslate.equals("Spanish")) {
                    regionToTranslate = "ES";
                    languageToTranslate = "es";
                } else if (longLanguageToTranslate.equals("Swahili")) {
                    regionToTranslate = "TZ";
                    languageToTranslate = "sw";
                } else if (longLanguageToTranslate.equals("Swedish")) {
                    regionToTranslate = "SE";
                    languageToTranslate = "sv";
                } else if (longLanguageToTranslate.equals("Tagalog")) {
                    regionToTranslate = "PH";
                    languageToTranslate = "tl";
                } else if (longLanguageToTranslate.equals("Tamil")) {
                    regionToTranslate = "IN";
                    languageToTranslate = "ta";
                } else if (longLanguageToTranslate.equals("Thai")) {
                    regionToTranslate = "TH";
                    languageToTranslate = "th";
                } else if (longLanguageToTranslate.equals("Turkish")) {
                    regionToTranslate = "TR";
                    languageToTranslate = "tr";
                } else if (longLanguageToTranslate.equals("Ukrainian")) {
                    regionToTranslate = "UA";
                    languageToTranslate = "uk";
                } else if (longLanguageToTranslate.equals("Urdu")) {
                    regionToTranslate = "PK";
                    languageToTranslate = "ur";
                } else if (longLanguageToTranslate.equals("Vietnamese")) {
                    regionToTranslate = "VN";
                    languageToTranslate = "vi";
                } else if (longLanguageToTranslate.equals("Welsh")) {
                    regionToTranslate = "GB";
                    languageToTranslate = "cy";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected if needed
            }
        });

        gpt3Api = new Gpt3Api(this);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        chatLayout = findViewById(R.id.layoutInput);
        progressBar = findViewById(R.id.progressBar);

        speechRecognizer = new SpeechRecognizer(this, REQUEST_CODE, this);
        textToSpeech = new TextToSpeech(this, this);
        isVoiceEnabled = getSharedPreferences("MICHATGPT", MODE_PRIVATE).getBoolean("CHECKRESPVOICE", true);

        Button buttonAsk = findViewById(R.id.buttonAsk);
        buttonAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prompt = editTextQuestion.getText().toString();
                editTextQuestion.setText("");
                callChatGpt("Translate the following " + languageTranslated + "text to "+ longLanguageToTranslate +": " + prompt);
            }
        });

        Button buttonSpeech = findViewById(R.id.buttonSpeech);
        buttonSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, regionTranslated);
                // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechRecognizer.start(regionTranslated);
                //voiceRecognitionLauncher.launch(intent);


                // Create a new RecognizerIntent with the specified language
                //Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, regionTranslated);
                //speechRecognizer.startListening();
            }
        });

        CheckBox checkboxVoiceResult = findViewById(R.id.checkVoiceResut);
        checkboxVoiceResult.setChecked(isVoiceEnabled);
        checkboxVoiceResult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isVoiceEnabled = isChecked;
                getSharedPreferences("MICHATGPT", MODE_PRIVATE).edit().putBoolean("CHECKRESPVOICE", isChecked).apply();
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
        TextView textView = new TextView(this);
        textView.setText(message);
        if (MESSAGE_TYPE_RESPONSE.equals(type)){
            textView.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 5, 30, 5);
        textView.setLayoutParams(params);
        chatLayout.addView(textView);
        if (MESSAGE_TYPE_RESPONSE.equals(type)){
            callTextToSpeech(message, new Locale(regionToTranslate, languageToTranslate));
        }
    }

    private void callChatGpt(String prompt){
        addChatMessage(prompt, MESSAGE_TYPE_REQUEST);
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
        callChatGpt("Translate the following " + languageTranslated + " text to " + longLanguageToTranslate + ": " + text);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale languageLocale = new Locale(languageToTranslate, regionToTranslate);
            int result = textToSpeech.setLanguage(languageLocale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Lenguaje no soportado");
            }
        } else {
            Log.e("TextToSpeech", "Inicialización fallida");
        }
    }

    private void callTextToSpeech(String text, Locale languageLocale) {
        if (isVoiceEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.setLanguage(languageLocale);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.setLanguage(languageLocale);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
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
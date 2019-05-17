// package com.deathrow.mymachine;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.InputStream;

// import edu.cmu.sphinx.api.Configuration;
// import edu.cmu.sphinx.api.SpeechResult;
// import edu.cmu.sphinx.api.LiveSpeechRecognizer;
// import edu.cmu.sphinx.api.StreamSpeechRecognizer;

// public class TranscriberDemo {       

//     public static void main(String[] args) throws Exception {

//         Configuration configuration = new Configuration();

//         configuration
//                 .setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
//         configuration
//                 .setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
//         configuration
//                 .setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
// LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
// // Start recognition process pruning previously cached data.
// recognizer.startRecognition(true);
// System.out.println("You can start to speak...\n");
// String speech_text = "";
// SpeechResult result = recognizer.getResult();
// // Pause recognition process. It can be resumed then with startRecognition(false).
// recognizer.stopRecognition();
//         while ((result = recognizer.getResult()) != null) {
//             speech_text = result.getHypothesis();

//             // String org_text = speech_text;
//             // speech_text = efilter.Filter(speech_text);
//             System.out.println(speech_text);
//         }
//         recognizer.stopRecognition();
//     }
// }
package cyf.gradle.api.service;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;

import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * speech
 *
 * @author Cheng Yufei
 * @create 2017-08-21 11:15
 **/
@Service
public class SpeechService {
    public void test() {
    /*    try {
            SpeechClient speechClient = SpeechClient.create();
            RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.FLAC;
            int sampleRateHertz = 44100;
            String languageCode = "zh-CN";
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(encoding)
                    .setSampleRateHertz(sampleRateHertz)
                    .setLanguageCode(languageCode)
                    .build();
            String uri = "gs://bucket_name/file_name.flac";
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setUri(uri)
                    .build();
            RecognizeResponse response = speechClient.recognize(config, audio);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public void test_2() {
        try {
            SpeechClient speech = SpeechClient.create();

            String fileName = "D:/audio.raw"; // for example "./resources/audio.raw";

            // Reads the audio file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result: results) {
                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
                for (SpeechRecognitionAlternative alternative: alternatives) {
                    System.out.printf("Transcription: %s%n", alternative.getTranscript());
                }
            }
            speech.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

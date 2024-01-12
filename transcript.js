const speech = require('@google-cloud/speech');
const fs = require('fs');

// Creates a client
const client = new speech.SpeechClient();

// Reads a local audio file and converts it into base64
const file = fs.readFileSync('/Users/blaadayoussef/captions/ahmed-2024.wav');
const audioBytes = file.toString('base64');

// Request configuration
const audio = {
  content: audioBytes,
};

const config = {
  encoding: 'LINEAR16',
  sampleRateHertz: 44100,
  languageCode: 'ar-MA', // Set the language to Arabic (Morocco)
  enableWordTimeOffsets: true, // Request word-level timestamps
  audioChannelCount: 2,
};

const request = {
  audio: audio,
  config: config,
};



function convertToASS(transcription) {
    let assContent = `[Script Info]
  Title: YourTitle
  ScriptType: v4.00+
  PlayResX: 1920
  PlayResY: 1080
  WrapStyle: 0
  
  [V4+ Styles]
  Format: Name, Fontname, Fontsize, PrimaryColour, Alignment, MarginV
  Style: Default, HONOR Sans Arabic UI, 54, &H00cf1f, 2, 300
  
  [Events]
  Format: Layer, Start, End, Style, Text\n`;
  
    let subtitleNumber = 1;
  
    for (const caption of transcription) {
      for (let i = 0; i < caption.words.length; i++) {
            const word = caption.words[i];
            const startTime = formatTimeAss(word.startTime);
            const endTime = formatTimeAss(word.endTime);
            const text = `{\\shad2}${word.word}`
            assContent += `Dialogue: 0, ${startTime}, ${endTime}, Default, ${text}\n`;
            subtitleNumber++;
      }
    }
  
    return assContent;
  }
  
  // Function to format time into ASS time format (HH:MM:SS.MILLISECONDS)
  function formatTimeAss(seconds) {
    const hours = 0;
    const minutes = 0;
    const milliseconds = String(seconds).split('.')
    const firstPart = milliseconds[0]
    const secondPart = milliseconds[1] ?? '0'
  
    return (
      `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${firstPart.padStart(2, '0')}.${secondPart.padEnd(2, '0')}`
    );
  }
  


function highlitghWord(words, currentWord) {
    let highlighted = ""
    for(const word of words) {
        if (word.word === currentWord) {
            highlighted += `<i>${currentWord}</i>`
        } else {
            highlighted += word.word
        }
    }
    return highlighted
}

function convertToSRT(transcription) {
    let srtContent = '';
    let subtitleNumber = 1;
    
    for (const caption of transcription) {
        for(let i = 0; i < caption.words.length; i += 3) {
            const word = caption.words[i];
            const startTime = word.startTime;
            const endTime = word.endTime;
            const highlighted = highlitghWord(caption.words.slice(i, i+3), word.word)
            srtContent += `${subtitleNumber}\n`;
            srtContent += `${formatTime(startTime)} --> ${formatTime(endTime)}\n`;
            srtContent += `${highlighted}\n\n`;
        
            subtitleNumber++;
        }  
    }
    return srtContent;
  }

function convert() {
  fs.readFile('output.json', 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      return;
    }
  
    const transcript = JSON.parse(data);
    const srtData = convertToASS(transcript);
    fs.writeFile('output.ass', srtData, (err) => {
        if (err) {
          console.error('Error writing SRT file:', err);
        } else {
          console.log('SRT file created successfully.');
        }
    });
  });
}
  
  // Function to format time into SRT time format (HH:MM:MILISECONDS)
  function formatTime(seconds) {
    const hours = 0;
    const minutes = 0;
    const miliseconds = seconds * 1000;
  
    return (
      `${String(hours).padStart(2, '0')}:` +
      `${String(minutes).padStart(2, '0')}:` +
      `${miliseconds.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`
    );
  }

// Performs speech recognition
client.recognize(request)
  .then((response) => {
    const transcription = response[0].results
    console.log(JSON.stringify(transcription))
    .map(result => {
    
    const alternatives = result.alternatives[0];
    
    const startTime = alternatives.words[0].startTime.seconds + alternatives.words[0].startTime.nanos / 1e9;
    const endTime = alternatives.words[alternatives.words.length - 1].endTime.seconds + alternatives.words[alternatives.words.length - 1].endTime.nanos / 1e9;
    const words = alternatives.words.map(word => {
        return {
        word: word.word,
        startTime: Number(word.startTime.seconds) + Number(word.startTime.nanos) / 1e9,
        endTime: Number(word.endTime.seconds) + Number(word.endTime.nanos) / 1e9
        };
    });
    return {
        transcript: alternatives.transcript,
        startTime,
        endTime,
        words,
    };
    });
    
    const srtData = convertToSRT(transcription);
    fs.writeFile('output.srt', srtData, (err) => {
        if (err) {
          console.error('Error writing SRT file:', err);
        } else {
          console.log('SRT file created successfully.');
        }
    });
    // Handle transcription data as needed
  })
  .catch((err) => {
    console.error('ERROR:', err);
  });   


  // convert()

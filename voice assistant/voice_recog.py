import speech_recognition as sr
import pyttsx3
#from ecapture import ecapture as ec

engine=pyttsx3.init('sapi5')
voices=engine.getProperty('voices')
engine.setProperty('voice','voices[1].id')

def speak(text):
    engine.say(text)
    engine.runAndWait()


def takeCommand():
    r=sr.Recognizer()
    with sr.Microphone() as source:
        print("I am Listening...")
        audio=r.listen(source)

        try:
            statement=r.recognize_google(audio,language='en-in')
            print(f"user said:{statement}\n")

        except Exception as e:
            speak("i couldnt understand that , try again")
            return "None"
        return statement


if __name__=='__main__':


    while True:
        
        speak("hello , Tell me how can I help you today?")
        
        statement = takeCommand().lower()
    
        if "good bye" in statement or "ok bye" in statement or "stop" in statement:
                speak('your personal assistant G-one is shutting down,Good bye')
                print('your personal assistant G-one is shutting down,Good bye')
                break
        if "set up event" in statement or "create new event" in statement or "create event" in statement or "create" in statement or "create" in statement:
            speak('please tell me the date of the reminder')
            schedule = takeCommand().lower()
            date = schedule[0]
            month = schedule[1]
            year = schedule[2]
            speak(' what time?')
            time = takeCommand().lower()
            speak('event created!')
        if "goodbye" in statement or "ok bye" in statement or "stop" in statement:
                speak('your personal assistant G-one is shutting down,Good bye')
                print('your personal assistant G-one is shutting down,Good bye')
                break
        # else:
        #     speak("please try again")
        
    

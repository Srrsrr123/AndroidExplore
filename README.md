# AndroidExplore
This is the instruction for the paper AndroidExplore: Testing Android App with Curiosity-Driven Reinforcement Learning for ICSE.
## Getting Started
### Prerequisites
This project requires Python, Xposed, UIAutomator2 and Jacoco, and it was tested on the following versions:
```
Python 3.8
Xposed 3.1.1
UIAutomator2 2.16.19
Jacoco 0.8.10
```
### Preparing the runtime environment
#### Install Android Studio
We run project "Dump" and Android apps in Android Studio. More details about installation steps can be found at: https://developer.android.google.cn/codelabs/basic-android-kotlin-compose-install-android-studio
#### Install PyCharm
We run project "Q_Learning" in PyCharm. More details about installation steps can be found at: https://www.jetbrains.com/help/pycharm/installation-guide.html
#### Install Android Emulator
We install "Dump" and apps in Android emulator. More details about installation steps can be found at: https://www.memuplay.com/support.html.
The emulator needs to have Xposed installed in order for the "Dump" module to work.

## Basic Usage
### Run AndroidExplore
* Open the "Dump" app and the application under test and start the "Dump" module in Xposed.
* Forward the data from port 8888 on PC to port 8888 on Android.
```
adb forward tcp:8888 tcp:8888
```
* Enter the necessary parameter AUT (i.e. the package name of the tested app) in `/Q_Learning/main/DataBase.py`.
* Run `/Q_Learning/main/run_this.py` to start the test.
### Getting Code Coverage
#### Script 1 - pull the exec file from the emulator
```
adb pull /sdcard/appName.ec destination_address\\appName.ec
```
#### Script 2 - generate the HTML report
```
java -jar jacococli.jar report appName.ec --classfiles classfile_directory --sourcefiles sourcefile_directory --html report_html
```
The code coverage result in file "index.html" of "report_html"
### Collecting Failures
We collect system-level failures from Android Studio console. It is important to note that in cases where the system has lower robustness, user-level failures may also lead to system-level failures. For example, if user-level failures are effectively handled (e.g., through rigorous input field validation), system-level failures do not occur. Otherwise, AndroidExplore triggers and captures these failures. It's worth emphasizing that all failures reported in the console are manually inspected to ensure that the thrown exceptions and errors are real failures.
## Team
Hidden due to paper submission.

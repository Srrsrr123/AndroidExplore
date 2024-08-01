# AndroidExplore
This is the instruction for the paper AndroidExplore: Testing Android App with Curiosity-Driven Reinforcement Learning for ICSE.
## Getting Started
### Prerequisites
This project requires Python, Xposed and UIAutomator2, and it was tested on the following versions:
```
Python 3.8
Xposed 3.1.1
UIAutomator2 2.16.19
```
### Preparing the runtime environment
#### install Android Studio
For more information https://developer.android.google.cn/codelabs/basic-android-kotlin-compose-install-android-studio
#### install PyCharm
#### install Android emulator


## Basic Usage
### Getting Code Coverage
#### Script 1 - pull the exec file from the emulator
```
adb pull /sdcard/appName.ec destination_address\\appName.ec
```
#### Script 2 - generate the HTML report
```
java -jar jacococli.jar report appName.ec --classfiles /var/lib/jenkins/workspace/SpringBootBase/target/classes --sourcefiles /var/lib/jenkins/workspace/SpringBootBase/src/main/java --html report_html
```
### Collecting Failures

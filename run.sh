adb uninstall in.gvc.bluetooth
rm -rf app/build/output/apk/*
./gradlew assembleRelease
adb install app/build/outputs/apk/app-release.apk 
adb shell am start -n in.gvc.bluetooth/in.gvc.bluetooth.MainActivity

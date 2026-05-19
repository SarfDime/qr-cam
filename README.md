# QR Camera

A small Android app that registers as a camera app and immediately launches the Google ML Kit QR scanner when opened.

This app is designed to work with Android's camera shortcut flows, including the power-button camera action and secure lock screen camera.

## What it does

- exposes the standard camera intent actions
- forwards launch requests to Google ML Kit's QR scanner activity
- exits immediately so it does not remain in recent apps

## Notes

- The app itself does not scan QR codes directly.
- It relies on the installed Google QR scanner activity.
- If the scanner is unavailable, it falls back to a generic QR scan intent.

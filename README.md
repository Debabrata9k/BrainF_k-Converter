# BrainF_k Converter (Android App)

BrainF_k Converter is an Android application that converts between **Text ↔ Brainfuck**, featuring an ultra-optimized Brainfuck encoder, interpreter, and multiple output modes.

---

## 📥 Download APK

Download the latest release here:

👉 **[Download BrainF_k Converter APK](https://github.com/Debabrata9k/BrainF_k-Converter/releases/tag/latest/BrainF_k.Converter.apk)**

(Replace the link above after you publish a release.)

---

## ✨ Features

### ✔ Text ➝ Brainfuck Conversion
- Convert normal text into valid Brainfuck code.
- Two encoding modes:
  - Basic encoding
  - **Ultra Optimized Encoding (Loops, Multi-cell, Shortest BF)**

### ✔ Brainfuck ➝ Text Conversion
- Fully working Brainfuck interpreter.
- Supports nested loops, wrapping, and correct BF cell behavior.

### ✔ Optimization Engine

Uses advanced encoding logic:
- loop-based generation
- multi-factor multiplier
- delta encoding when shorter
- remainder optimization
- no unnecessary cell clearing
- peephole optimization

### ✔ Extra Features
- Copy Brainfuck output
- Share BF code with other apps
- Clear input/output
- Simple & clean UI

---

## 🧠 How Encoding Works

The optimized encoder automatically:
- finds the shortest sequence for each character
- uses multiplication loops when beneficial:

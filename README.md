# JustFocus

### A Distraction-Free Focus Timer for Linux Desktops

JustFocus is a minimalist and elegant desktop application designed to help you maintain focus during your work sessions.
Built with Java Swing, this unobtrusive timer sits cleanly on your desktop, providing clear visual feedback
without unnecessary distractions.

**Key Features:**

* **Minimalist Design:** A clean, undecorated circular UI that blends seamlessly with your desktop environment.
* **Visual Progress Indicator:** A smooth, animated arc visually represents the time remaining in your focus session,
  offering intuitive feedback at a glance.
* **Focus-Oriented:** Designed for dedicated work, JustFocus provides a fixed-duration timer (e.g., 30 minutes) without
  pause functionality, encouraging uninterrupted concentration.
* **Lightweight & Efficient:** Optimized for size and performance, making it a nimble addition to your workflow.

### Installation (Snap)

JustFocus is available as a Snap package for easy installation on most Linux distributions.

```bash
sudo snap install justfocus
```

### Technologies

- Java (Swing)

### Contributing

Contributions are welcome! If you have suggestions for features, bug reports, or would like to contribute code, please
feel free to open an issue or pull request on the GitHub repository.

## Build and execute the snap

```bash
sudo snap remove justfocus ; snapcraft clean; snapcraft; sudo snap install justfocus_*.*.*_amd64.snap --dangerous; justfocus
```

### License

This project is licensed under
the [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-nc-sa/4.0/).
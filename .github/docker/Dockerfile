# ─────────────────────────────────────────────────────────────────────────────
# CI image for Git-Hub-Popular-Search
#
# Provides: JDK 21 · Android SDK (platform 36 + build-tools) · Ruby + Bundler
#
# Build args (override with --build-arg when needed):
#   CMDLINE_TOOLS_BUILD – numeric build ID in the cmdline-tools archive name.
#                         Check https://developer.android.com/studio#command-tools
#   ANDROID_PLATFORM    – Android API level to install (default: 36)
#   ANDROID_BUILD_TOOLS – build-tools version to install  (default: 36.0.0)
# ─────────────────────────────────────────────────────────────────────────────
FROM ubuntu:24.04

ARG CMDLINE_TOOLS_BUILD=11076708
ARG ANDROID_PLATFORM=36
ARG ANDROID_BUILD_TOOLS=36.0.0

# ── Environment ───────────────────────────────────────────────────────────────
ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
# Persist the build-tools version so it is addressable at runtime too
ENV ANDROID_BUILD_TOOLS=${ANDROID_BUILD_TOOLS}
ENV PATH="${JAVA_HOME}/bin:\
${ANDROID_HOME}/cmdline-tools/latest/bin:\
${ANDROID_HOME}/platform-tools:\
${ANDROID_HOME}/build-tools/${ANDROID_BUILD_TOOLS}:\
${PATH}"

# ── System packages ───────────────────────────────────────────────────────────
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        openjdk-21-jdk \
        ruby-full \
        curl \
        unzip \
        wget \
        git \
        build-essential \
        libssl-dev \
        libffi-dev \
    && rm -rf /var/lib/apt/lists/* \
    # Install the latest Bundler via RubyGems (the apt package can be stale)
    && gem install bundler --no-document

# ── Android cmdline-tools ─────────────────────────────────────────────────────
RUN mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && wget -q \
        "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_BUILD}_latest.zip" \
        -O /tmp/cmdline-tools.zip \
    && unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdtools \
    && mv /tmp/cmdtools/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm -rf /tmp/cmdtools /tmp/cmdline-tools.zip

# ── Android SDK packages + licence acceptance ─────────────────────────────────
RUN yes | sdkmanager --licenses > /dev/null 2>&1 ; \
    sdkmanager \
        "platform-tools" \
        "platforms;android-${ANDROID_PLATFORM}" \
        "build-tools;${ANDROID_BUILD_TOOLS}"

WORKDIR /workspace


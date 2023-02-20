# Builds yocto zeus on new distros (requires python2)

FROM ubuntu:focal
RUN apt-get update
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ="America/Los_Angeles"
ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8
RUN apt-get install -y tzdata locales
RUN locale-gen en_US.UTF-8
RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
RUN apt-get install -y build-essential gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio vim
RUN apt-get install -y xz-utils debianutils iputils-ping libegl1-mesa libsdl1.2-dev pylint3 xterm mesa-common-dev
RUN apt-get install -y python2

ARG USERNAME=dev
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && apt-get update \
    && apt-get install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME
USER $USERNAME

WORKDIR /rpi4-yocto-xeoma-server

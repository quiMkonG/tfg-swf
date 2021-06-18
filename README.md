# tfg-swf

Desenvolupament d'una eina d'espacialització sonora basada en wavelets en el marc del meu treball de final de grau. Enginyeria en Sistemes Audiovisuals, UPF, Barcelona.

Development of a sound spatialization tool based on wavelets within my final degree project. Audiovisual Systems Engineering, UPF, Barcelona.

## Guia d'usuari

(Eng. below)
Aquest treball consta de dues eines principals, ambdues dissenyades per l'entorn de programació de SuperCollider. La primera és una llibreria que habilita el tractament espacial del so en l'espai tridimensional mitjançant la codificació i descodificació d'àudio a través de Sound Wavelets (ref. Davide i Dani). La segona és una breu interfície d'interacció amb l'usuari per tal de poder localitzar fonts sonores en l'espai a temps real mitjançant VBAP, Ambisonics o Sound Wavelets (SWF). En aquest repositori hi trobaràs:

1.- mesh plots: en aquesta carpeta hi trobaràs il·lustracions de la malla de 66 vèrtexs sobre la qual es treballa, així com els índex dels vèrtex que formen un triangle entre ells.

2.- spatial-audio-interface: la interfície d'usuari esmentada anteriorment. Tots els mètodes estan implementats per funcionar amb una maquetació d'altaveus 9.6 (tret de VBAP, que també inclou estèreo). Per Ambisonics i SWF estan habilitats els ordres 1 i 3, i els nivells 0 i 1 respectivament. Per tal d'utilitzar la interfície d'usuari l'únic que cal és iniciar una instància de la classe Spatial-Audio. Hi trobareu també una subcarpeta que conté una classe auxiliar per descodificar Ambisonics a 9.6, i les matrius corresponents elaborades amb IDHOA.

```
x = SpatialAudio.new();
```

3.- swf: llibreria que conté les classes i mètodes necessaris per treballar amb SWF. També hi trobareu una subcarpeta amb matrius auxiliars utilitzades per aquesta llibreria. La majoria de les matrius han estat extretes de: https://github.com/davrandom/swf. D'altres han estat elaborades per mi mateix i les matrius de descodificació han estat elaborades a través de IDHOA.

ATENCIÓ: les carpetes de spatial-audio i swf s'han de guardar al directori resultant d'executar <Platform.userExtensionDir;> dins de SuperCollider per tal que puguin funcionar. 

```
Platform.userExtensionDir;
```

### Pre requisits

Per la llibreria de SWF no és necessari cap requisit a nivell de software. Tot i això, la descodificació està implmementada per una maquetació d'altaveus 9.6.

Per utilitzar la interfície d'usuari cal tenir instal·lat el quark de SC-HOA de SuperCollider (https://github.com/florian-grond/SC-HOA). És important mencionar que la interfície serveix per localitzar fonts monofòniques (en cas de fitxers d'àudio, SC només accepta els formats .wav i .aiff.

### Ús de la llibreria

Hi ha 3 classes. SWF, SWFEncoder i SWFDecoder. Les dues últimes hereten de la primera. El funcionament és el següent.

1.- Declarar una instància de la classe SWFEncoder.

2.- De la instància declarada anteriorment, cridar el mètode _getCoarses_lvl0_ o _getCoarses_lvl1_ segons el nivell de SWF amb què es vulgui treballar. Com a paràmetres li passem les posicions angulars de la font (azi, ele), i ens retornarà un vector de coarse.

3.- Amb una instància de SWF.ar(in, coarse), obtindrem el senyal resultant a punt per ser emès als busos de SuperCollider.

```
x = SWFEncoder.new();
result = SWF.ar(input signal, x.getCoarses_lvl0(azi, ele));
```

## User's Guide

This project consists of two main tools, both designed for the SuperCollider programming environment. The first one is a library that enables the spatial treatment of sound in the three dimensional space through the encoding and decoding of audio using SoundWavelets (inserir ref. davide i dani). The second one is a brief user interaction interface to be able to localize sound sources in space in real-time using either VBAP, Ambisonics or Sound Wavelets (SWF). In this repo, you will find:

1.- mesh plots: in this folder you will find figures of the mesh of 66 vertices on which the rest of the library relies, as well as the vertices' indices that form a triangle.

2.- spatial-audio-interface: the user interface mentioned above. All methods are implemented to work in a 9.6 loudspeaker layout (unless VBAP, which also includes stereo). For Ambisonics and SWF, orders 1 and 3 are enabled for the former, and levels 0 and 1 are available for the latter. In order to use the user interface, the only thing you need to do is initialize an instance of the class SpatialAudio. You will find another subfolder containing an auxiliar class to decode Ambisonics to 9.6, and the corresponding matrices elaborated with IDHOA.

```
x = SpatialAudio.new();
```

3.- swf: the library that contains the necessary classes and methods to work with SWF. You will also find a subfolder with auxiliar matrices used by this library. Most of these matrices have been extracted from:  https://github.com/davrandom/swf. The others have been either designed by myself, or elaborated through IDHOA (decoding matrices).

WARNING: the spatial-audio and swf folders must be stored in the user extension directory of SuperCollider. To know where it is, just open SC and execute the following:

```
Platform.userExtensionDir;
```

### Previous Requirements

For the SWF library it is not necessary any software requirement. However, note that de descoding is implemented for a 9.6 loudpspeaker layout.

In order to use the user interface, it is necessary to have installed the SC-HOA quark (https://github.com/florian-grond/SC-HOA). It is important to mention that the interface is used to localize monophonic sound sources (in case of audio files, SC only accepts .wav and .aiff formats).

### Use of the library

There are 3 classes, SWF, SWFEncoder and SWFDecoder. The last two intherit from the first. It works the following way.

1.- Declare an instance of the SWFEncoder class.

2.- Out of the instance declared above, use the method _getCoarses_lvl0_ or _getCoarses_lvl1_ depending on the SWF level you want to work with. As input parameters, this method needs the angular positions of the source (azi, ele) and it will return a coarse vector.

3.- With an instance of SWF.ar(in, coarse), you will obtain the resulting signal ready to be sent to the different SC buses.

```
x = SWFEncoder.new();
result = SWF.ar(input signal, x.getCoarses_lvl0(azi, ele));
```

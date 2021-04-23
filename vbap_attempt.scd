s.boot;

//p = Platform.resourceDir +/+ "sounds/a11wlk01.wav";
~sound = Buffer.read(s,"C:\Users\usuario\Desktop\Quim\UNI\TFG\Recursos\piano_sample.mp3");
//~sound.play;
//~sound.numChannels;


//numSamples = numFrames * numChannels

// 2D

a = VBAPSpeakerArray.new(2, [-30,30]); // stereophonic set

b = a.loadToBuffer;
b.numChannels;
b.bufnum;
(
x = { |azi = 0, ele = 0, spr = 0|
	VBAP.ar(2, PlayBuf.ar(2, ~sound.bufnum), b.bufnum, azi, ele, spr);
}.scope;
)

s.plotTree;

// test them out
{[-30, -15, 0, 15, 30, 0].do({|ang| x.set(\azi, ang); 3.wait; }) }.fork;

// try the spread
x.set(\spr, 20);
x.set(\spr, 100); // all speakers

x.free; b.free;
s.freeAll;


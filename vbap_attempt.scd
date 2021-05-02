s.boot;

(
//2D VBAP
var lsp, a, b;
lsp = [-30,30];
a = VBAPSpeakerArray.new(2, lsp);
b = a.loadToBuffer;

~walk = Buffer.read(s,"C:/Users/usuario/Desktop/Quim/UNI/TFG/Recursos/118985__ragamuffin__male-foot-walk-marble-stereo.aiff");
~sound = Buffer.read(s,"C:/Users/usuario/Desktop/Quim/UNI/TFG/Recursos/522810__sarafg11__piano1-ex1-good-good.wav");
//~sound.numChannels;

(
SynthDef.new(\vbap_2D, {
	arg amp = 1, out = 0, da = 2, rate = 1, buf, azi, ele = 0, spr = 0;
	var sig, source;
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	sig = VBAP.ar(2, source, b.bufnum, azi.lag(0.5), ele, spr);
	Out.ar(out, sig);
}).add;
);


Window.closeAll;

//GUI
w = Window.new("2D VBAP Panner", Rect(800, 400, 400, 450)).front;
w.view.background_(Color.grey(0.8));
w.view.decorator = FlowLayout(w.view.bounds);
h = HLayout();
v = VLayout(h);
w.layout =  v;
w.front;
w.alwaysOnTop = true;


//ON/OFF SWITCHER
b = Button(w, Rect(20, 20, 340, 30))
.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			x = Synth.new(\vbap_2D, [\buf, ~sound.bufnum, \azi, k.value.linlin(0,1,-30,30).round]).register;
		},{x.free}
	);
});


//TEXT VIEW
h.add(g = VLayout(), 2, \left);
g.add(b);
g.add(t = TextView());

x = t.selectedString;

//SLIDER
v.add(k = Slider());
k.orientation = \horizontal;

k.action_({
	arg obj;
	var angle;
	angle = obj.value.linlin(0,1,-30,30).round.postln;
	if(
		x.isPlaying,{x.set(\azi, angle)}
	);
});
)


//3D VBAP
(
var lsp, a, b;
lsp = [[40,0],[-40,0],[80,0],[-80,0],[120,0],[-120,0],[160,0],[-160,0],[0,0],[24,21],[-24,21],[90,26],[-90,30],[154,24.5],[-155,24.5],[20,-12]];//CANVIAR EL SET D'ALTAVEUS
a = VBAPSpeakerArray.new(3, lsp);
b = Buffer.loadCollection(s, a.getSetsAndMatrices);

~walk = Buffer.read(s,"C:/Users/usuario/Desktop/Quim/UNI/TFG/Recursos/118985__ragamuffin__male-foot-walk-marble-stereo.aiff");
~sound = Buffer.read(s,"C:/Users/usuario/Desktop/Quim/UNI/TFG/Recursos/522810__sarafg11__piano1-ex1-good-good.wav");
//~sound.numChannels;

(
SynthDef.new(\vbap_3D, {
	arg amp = 1, out = 0, da = 2, rate = 1, buf, azi, ele, spr;
	var sig, source;
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	sig = VBAP.ar(2, source, b.bufnum, azi, ele, spr);//CANVIAR OUTPUT CHANNELS
	Out.ar(out, sig);
}).add;
);

//Synth.new(\vbap_3D, [\buf, ~sound.bufnum, \azi, 30, \ele, 0, \spr, 0]);

Window.closeAll;
//GUI
w = Window.new("3D VBAP Panner", Rect(800, 400, 400, 450)).front;
w.view.background_(Color.grey(0.8));
w.view.decorator = FlowLayout(w.view.bounds);
h = HLayout();
v = VLayout(h);
w.layout =  v;
w.front;
w.alwaysOnTop = true;


//ON/OFF SWITCHER
b = Button(w, Rect(20, 20, 340, 30))
.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			x = Synth.new(\vbap_3D, [\buf, ~sound.bufnum, \azi, k.value.linlin(0,1,-180,180).round, \ele, l.value.linlin(0,1,-90,90).round]).register;
		},{x.free}
	);
});


//TEXT VIEW
h.add(g = VLayout(), 2, \left);
g.add(b);
g.add(t = TextView());

x = t.selectedString;

//SLIDER FOR AZIMUTH
v.add(k = Slider());
k.orientation = \horizontal;

k.action_({
	arg obj;
	var angle;
	angle = obj.value.linlin(0,1,-180,180).round;
	if(
		x.isPlaying,{x.set(\azi, angle)}
	);
});

//SLIDER FOR ELEVATION
v.add(l = Slider());
l.orientation = \horizontal;

l.action_({
	arg obj;
	var elev;
	elev = obj.value.linlin(0,1,-90,90).round.postln;
	if(
		x.isPlaying(x.set(\ele, elev))
	);
});
)


s.boot;

//numSamples = numFrames * numChannels

//SET SALA ARANYÓ
/*o = Server.default.options;
o.outDevice_("ASIO : Dante Virtual Soundcard (x64)");
Server.default.reboot;
)
ServerOptions.numOutputBusChannels;
s.plotTree;
s.meter;
ServerOptions.devices; // all devices
ServerOptions.inDevices; // input devices
ServerOptions.outDevices; // output devices
*/

//3D

//3D VBAP
(
var lsp, a, b, dim;
var sound01, sound02, sound03;
var drop_01, drop_02, drop_03;//drop buttons
var add_01, add_02, add_03;//add file buttons
var p01, p02, p03, pAll;//play buttons
var tazi01, telev01, tazi02, telev02, tazi03, telev03, volume;//dynamic text for azimuths and elevations values
var sazi01, sazi02, sazi03, selev01, selev02, selev03;//sliders for azimuth and elevation
var v01, v02, v03;//sliders for volume
var leftLayout, centerLayout, rightLayout;
var dropText, fileText, playText;
var source01Layout, source02Layout, source03Layout;
var vbapWindow;

lsp = [[40,0],[-40,0],[80,0],[-80,0],[120,0],[-120,0],[160,0],[-160,0],[0,0],[24,21],[-24,21],[90,26],[-90,30],[154,24.5],[-155,24.5],[20,-12]]; dim = 3;//CANVIAR EL SET D'ALTAVEUS
//lsp = [-30, 30]; dim = 2;
a = VBAPSpeakerArray.new(dim, lsp);
b = Buffer.loadCollection(s, a.getSetsAndMatrices);

//SYNTHDEFS
(
SynthDef.new(\vbap_3D_01, {
	arg amp, out = 0, da = 2, rate = 1, buf, azi, ele, spr;
	var sig, source;
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	sig = VBAP.ar(lsp.size, source, b.bufnum, azi, ele, spr);//CANVIAR OUTPUT CHANNELS
	Out.ar(out, sig);
}).add;
);

(
SynthDef.new(\vbap_3D_02, {
	arg amp, out = 0, da = 2, rate = 1, buf, azi, ele, spr;
	var sig, source;
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	sig = VBAP.ar(lsp.size, source, b.bufnum, azi, ele, spr);//CANVIAR OUTPUT CHANNELS
	Out.ar(out, sig);
}).add;
);

(
SynthDef.new(\vbap_3D_03, {
	arg amp, out = 0, da = 2, rate = 1, buf, azi, ele, spr;
	var sig, source;
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	sig = VBAP.ar(lsp.size, source, b.bufnum, azi, ele, spr);//CANVIAR OUTPUT CHANNELS
	Out.ar(out, sig);
}).add;
);


Window.closeAll;



//FROM NOW ON, THERE IS THE GUI DEVELOPMENT
vbapWindow = Window.new("3D VBAP Panner", Rect(400, 200, 800, 800)).front;
vbapWindow.view.background_(Color.grey(0.8));
vbapWindow.view.decorator = FlowLayout(vbapWindow.view.bounds);
h = HLayout();
v = VLayout(h);
vbapWindow.layout =  v;
vbapWindow.front;
vbapWindow.alwaysOnTop = true;




//DRAG&DROP FOR AUDIO FILES
h.add(leftLayout = VLayout(), 3, \left);
h.add(centerLayout = VLayout(), 3, \center);
h.add(rightLayout = VLayout(), 3, \right);

leftLayout.add(dropText = StaticText(w));
dropText.string = "Drop the sound files below (.wav/.aiff)";
leftLayout.add(drop_01 = DragSink());
leftLayout.add(drop_02 = DragSink());
leftLayout.add(drop_03 = DragSink());

drop_01.background = Color(1,0.5,0);
drop_02.background = Color(0,0.6,0.6);
drop_03.background = Color(0.3,0.6,0);

drop_01.receiveDragHandler = {arg obj; obj.object = View.currentDrag.shellQuote};
drop_02.receiveDragHandler = {arg obj; obj.object = View.currentDrag.shellQuote};
drop_03.receiveDragHandler = {arg obj; obj.object = View.currentDrag.shellQuote};





//ADD FILE BUTTON
centerLayout.add(fileText = StaticText(w));
fileText.string = "Once file is dropped, click on ADD";
add_01 = Button(w)
.states_([["ADD", Color.black, Color.white],["FILE ADDED", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,{
			sound01 = Buffer.read(s,drop_01.string.replace("'",""););
		}
	);
});

add_02 = Button(w)
.states_([["ADD", Color.black, Color.white],["FILE ADDED", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,{
			sound02 = Buffer.read(s,drop_02.string.replace("'",""););
		}
	);
});

add_03 = Button(w)
.states_([["ADD", Color.black, Color.white],["FILE ADDED", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,{
			sound03 = Buffer.read(s,drop_03.string.replace("'",""););
		}
	);
});





//ON/OFF SWITCHER
rightLayout.add(playText = StaticText(w));
playText.string = "Then click on play";

p01 = Button(w, Rect(20, 20, 340, 30))
.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			x = Synth.new(\vbap_3D_01, [\amp, v01.value,\buf, sound01.bufnum, \azi, sazi01.value.linlin(0,1,-180,180).round, \ele, selev01.value.linlin(0,1,-90,90).round]).register;
		},{x.free}
	);
});

p02 = Button(w, Rect(20, 20, 340, 30))
.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			y = Synth.new(\vbap_3D_02, [\amp, v02.value,\buf, sound02.bufnum, \azi, sazi02.value.linlin(0,1,-180,180).round, \ele, selev02.value.linlin(0,1,-90,90).round]).register;
		},{y.free}
	);
});

p03 = Button(w, Rect(20, 20, 340, 30))
.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			z = Synth.new(\vbap_3D_03, [\amp, v03.value,\buf, sound03.bufnum, \azi, sazi03.value.linlin(0,1,-180,180).round, \ele, selev03.value.linlin(0,1,-90,90).round]).register;
		},{z.free}
	);
});

pAll = Button(w)
.states_([["PLAY ALL", Color.black, Color.white],["PAUSE ALL", Color.black, Color.white]])
.action_({
	arg obj;
	if(
		obj.value == 1,
		{
			p01.valueAction = 1;
			p02.valueAction = 1;
			p03.valueAction = 1;
		},{p01.valueAction=0;p02.valueAction=0;p03.valueAction=0;}
	);
});

//SHOW BUTTONS
centerLayout.add(add_01);centerLayout.add(add_02);centerLayout.add(add_03);
rightLayout.add(p01);rightLayout.add(p02);rightLayout.add(p03);


//SLIDERS PLACEMENT
v.add(k = HLayout());
k.add(source01Layout = VLayout(), 2, \left);
k.add(source02Layout = VLayout(), 2, \center);
k.add(source03Layout = VLayout(), 2, \right);


//SLIDERS FOR AZIMUTH

//SOURCE 1
source01Layout.add(tazi01 = StaticText(w));
tazi01.string = "Azimuth angle [-180, 180]";

source01Layout.add(sazi01 = Slider());
sazi01.orientation = \horizontal;
//sazi01.Color(1,0.5,0);

sazi01.action_({
	arg obj;
	var angle;
	if(dim == 2, {angle = obj.value.linlin(0,1,-30,30).round;}, {});
	if(dim == 3, {angle = obj.value.linlin(0,1,-180,180).round;}, {});

	tazi01.string = "Azimuth angle [-180, 180] = "++ angle;
	if(
		x.isPlaying,{x.set(\azi, angle)}
	);
});


//SOURCE 2
source02Layout.add(tazi02 = StaticText(w));
tazi02.string = "Azimuth angle [-180, 180]";

source02Layout.add(sazi02 = Slider());
sazi02.orientation = \horizontal;

sazi02.action_({
	arg obj;
	var angle;
	if(dim == 2, {angle = obj.value.linlin(0,1,-30,30).round;}, {});
	if(dim == 3, {angle = obj.value.linlin(0,1,-180,180).round;}, {});

	angle = obj.value.linlin(0,1,-180,180).round;
	tazi02.string = "Azimuth angle [-180, 180] = "++ angle;
	if(
		y.isPlaying,{y.set(\azi, angle)}
	);
});


//SOURCE 3
source03Layout.add(tazi03 = StaticText(w));
tazi03.string = "Azimuth angle [-180, 180]";

source03Layout.add(sazi03 = Slider());
sazi03.orientation = \horizontal;

sazi03.action_({
	arg obj;
	var angle;
	if(dim == 2, {angle = obj.value.linlin(0,1,-30,30).round;}, {});
	if(dim == 3, {angle = obj.value.linlin(0,1,-180,180).round;}, {});

	angle = obj.value.linlin(0,1,-180,180).round;
	tazi03.string = "Azimuth angle [-180, 180] = "++ angle;
	if(
		z.isPlaying,{z.set(\azi, angle)}
	);
});

sazi01.background = Color(1,0.5,0);
sazi02.background = Color(0,0.6,0.6);
sazi03.background = Color(0.3,0.6,0);


//SLIDER FOR ELEVATION

//SOURCE 1
source01Layout.add(telev01 = StaticText(w));
telev01.string = "Elevation angle [-90, 90]";

source01Layout.add(selev01 = Slider());
selev01.orientation = \vertical;

selev01.action_({
	arg obj;
	var elev;
	elev = obj.value.linlin(0,1,-90,90).round;
	if(dim == 2, {elev = 0}, {});
	if(dim == 3, {elev = obj.value.linlin(0,1,-180,180).round;}, {});

	telev01.string = "Elevation angle [-90, 90] = "++ elev;
	if(
		x.isPlaying,{x.set(\ele, elev)}
	);
});


//SOURCE 2
source02Layout.add(telev02 = StaticText(w));
telev02.string = "Elevation angle [-90, 90]";

source02Layout.add(selev02 = Slider());
selev02.orientation = \vertical;

selev02.action_({
	arg obj;
	var elev;
	if(dim == 2, {elev = 0}, {});
	if(dim == 3, {elev = obj.value.linlin(0,1,-180,180).round;}, {});

	elev = obj.value.linlin(0,1,-90,90).round;
	telev02.string = "Elevation angle [-90, 90] = "++ elev;
	if(
		y.isPlaying,{y.set(\ele, elev)}
	);
});

//SOURCE 3
source03Layout.add(telev03 = StaticText(w));
telev03.string = "Elevation angle [-90, 90]";

source03Layout.add(selev03 = Slider());
selev03.orientation = \vertical;

selev03.action_({
	arg obj;
	var elev;
	if(dim == 2, {elev = 0}, {});
	if(dim == 3, {elev = obj.value.linlin(0,1,-180,180).round;}, {});

	elev = obj.value.linlin(0,1,-90,90).round;
	telev03.string = "Elevation angle [-90, 90] = "++ elev;
	if(
		z.isPlaying,{z.set(\ele, elev)}
	);
});

selev01.background = Color(1,0.5,0);
selev02.background = Color(0,0.6,0.6);
selev03.background = Color(0.3,0.6,0);



//SLIDER FOR VOLUME

//SOURCE 1
source01Layout.add(volume = StaticText(w));
volume.string = "Volume";

source01Layout.add(v01 = Slider());
v01.orientation = \vertical;

v01.action_({
	arg obj;
	var volume;
	volume = obj.value;
	if(
		x.isPlaying,{x.set(\amp, volume)}
	);
});

//SOURCE 2
source02Layout.add(volume = StaticText(w));
volume.string = "Volume";

source02Layout.add(v02 = Slider());
v02.orientation = \vertical;

v02.action_({
	arg obj;
	var volume;
	volume = obj.value;
	if(
		y.isPlaying,{y.set(\amp, volume)}
	);
});

//SOURCE 3
source03Layout.add(volume = StaticText(w));
volume.string = "Volume";

source03Layout.add(v03 = Slider());
v03.orientation = \vertical;

v03.action_({
	arg obj;
	var volume;
	volume = obj.value;
	if(
		z.isPlaying,{z.set(\amp, volume)}
	);
});

v01.background = Color(1,0.5,0);
v02.background = Color(0,0.6,0.6);
v03.background = Color(0.3,0.6,0);

)

Platform.userExtensionDir;
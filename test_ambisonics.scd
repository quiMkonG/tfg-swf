
s.boot;
s.plotTree;
Platform.userExtensionDir;
Platform.systemExtensionDir;

//SALA ARANYÓ
(
o = Server.default.options;
o.numOutputBusChannels = 16;
Server.default.reboot;
)

s.meter;

~sound = Buffer.read(s,"C:/Users/usuario/Desktop/Quim/UNI/TFG/Recursos/522810__sarafg11__piano1-ex1-good-good.wav");
(
var azimuth_lsp, elevation_lsp, setup_name, format_counter=0;

azimuth_lsp = [40, -40, 80, -80, 120, -120, 160, -160, 0, 24, -24, 90, -90, 154, -155, 20];
elevation_lsp = [0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 21, 26, 30, 24.5, 24.5, -12];
elevation_lsp.size.postln;

for (0, elevation_lsp.size-1,
	{arg i;
		if(elevation_lsp.at(i)==0,{format_counter = format_counter + 1},{});
});
setup_name = format("%.%",format_counter, elevation_lsp.size-format_counter-1);


DecodeAmbi1O.addSetup(name: setup_name, azimuth: azimuth_lsp, elevation: elevation_lsp);
(
SynthDef.new(\ambi, {
	arg amp = 0.5, buf, rate = 1, da = 2, azi, out = 0;
	var sig, source, encoded;

	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
	encoded = PanAmbi1O.ar(in:source, azi: 30, elev: 0, level: 1);
	sig = DecodeAmbi1O.ar(encoded, setup: setup_name ,level:0.5);

	Out.ar(out,sig);
}).add;
)

)

x = Synth.new(\ambi,[\buf, ~sound.bufnum, \azi, -30]);
x.free;


/*
(
x = PanAmbi1O.ar(in:PinkNoise(0.2), azi:30, elev:0, level: 1);
y = DecodeAmbi1O.ar(in: x, setup: '1_4_7_4', level: 1);
)

(
var x, dec_matrix, f;
x = PanAmbi1O.ar(in:PinkNoise.ar(0.2), azi:30, elev:0, level: 1);
x.removeAt(3);
x.postln;
dec_matrix = [[0.171803, 0.615841*sqrt(3), 0.513763*sqrt(3)],[0.171784, 0.615815*sqrt(3), -0.513813*sqrt(3)]];//w, y, x
dec_matrix = dec_matrix.flop;
f = x * dec_matrix;
f.postln;
)

(
SynthDef.new(\test, {
	arg amp = 0.5, buf, rate = 1, da = 2, azi;
	var result, dec_matrix, x, source;
	dec_matrix = FoaDecoderMatrix.newStereo(angle:30.degrad, pattern: 0.5);
	source = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da);
	x = PanAmbi1O.ar(in: source, azi:azi, elev:0, level: 1);
	//x.swap(1,2);
	result = FoaDecode.ar(x, dec_matrix);
	Out.ar(0, result);
}).add;
)

z = Synth.new(\test, [\buf, ~sound.bufnum, \azi, -30]);
z.free;

s.plotTree;
s.meter;

(
DecodeAmbi1O.addSetup(name: '9.6', azimuth: [40, -40, 80, -80, 120, -120, 160, -160, 0, 24, -24, 90, -90, 154, -155, 20], elevation: [0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 21, 26, 30, 24.5, 24.5, -12]);
)
*/

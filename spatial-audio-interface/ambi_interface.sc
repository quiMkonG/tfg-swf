AmbiInterface{
	classvar ambiWindow, order, s;

	*new{|ambiOrder|
		order = ambiOrder;
		s = Server.default;
		^super.new.init();
	}

	init{
		var v, h, userInterface;

		ambiWindow = Window.new("Ambisonics", Rect(
			Window.screenBounds.width/2-400,
			Window.screenBounds.height/2-100,
			800,
			200
		)).front;
		ambiWindow.view.background_(Color.grey(0.8));
		ambiWindow.view.decorator = FlowLayout(ambiWindow.view.bounds);
		h = HLayout();
		v = VLayout(h);
		ambiWindow.layout =  v;
		ambiWindow.front;

		if(order == 1, {

			(
			SynthDef.new(\ambi_01,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				//src = PinkNoise.ar(0.05);
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig,order);
				Out.ar(out, dec);
			}).add;
			);

			(
			SynthDef.new(\ambi_02,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig, order);
				Out.ar(out, dec);
			}).add;
			);

			(
			SynthDef.new(\ambi_03,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig, order);
				Out.ar(out, dec);
			}).add;
			);

		});

		if(order == 3, {

			(
			SynthDef.new(\ambi_01,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				//src = PinkNoise.ar(0.05);
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig,order);
				Out.ar(out, dec);
			}).add;
			);

			(
			SynthDef.new(\ambi_02,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig, order);
				Out.ar(out, dec);
			}).add;
			);

			(
			SynthDef.new(\ambi_03,{
				arg out = 0, buf, da = 2, rate = 1, amp, azi, ele;
				var src, sig, dec;
				src = PlayBuf.ar(numChannels: 1, bufnum: buf, rate: BufRateScale.kr(buf) * rate, doneAction: da)*amp;
				sig = HOAEncoder.ar(order, src, azi.degrad, ele.degrad);
				dec = DecAmbisonics.ar(sig, order);
				Out.ar(out, dec);
			}).add;
			);

		});

		userInterface = this.ambiWindowConfig(v,h);
	}


	ambiWindowConfig { |v,h|
		var sound01, sound02, sound03;//buffers for sound files

		var drop_01, drop_02, drop_03;//drop buttons
		var add_01, add_02, add_03;//add file buttons
		var p01, p02, p03, pAll;//play buttons

		var dropText, fileText, playText;//static texts of the window
		var volume;
		var tazi01, telev01, tazi02, telev02, tazi03, telev03, tvol01, tvol02, tvol03;//dynamic text for azimuths,elevation and volume values
		var sazi01, sazi02, sazi03, selev01, selev02, selev03;//sliders for azimuth and elevation
		var v01, v02, v03;//sliders for volume

		var slidersLayout;//sub Layout for the sliders
		var leftLayout, centerLayout, rightLayout;//main source layouts in the window
		var source01Layout, source02Layout, source03Layout;//sub layouts for each source sliders

		var synth01, synth02, synth03;//variables to declare the new synthdefs for each sound source

		//DRAG&DROP FOR AUDIO FILES
		h.add(leftLayout = VLayout(), 3, \left);
		h.add(centerLayout = VLayout(), 3, \center);
		h.add(rightLayout = VLayout(), 3, \right);

		leftLayout.add(dropText = StaticText(ambiWindow));
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
		centerLayout.add(fileText = StaticText(ambiWindow));
		fileText.string = "Once file is dropped, click on ADD";
		add_01 = Button(ambiWindow)
		.states_([["ADD", Color.black, Color.white],["FILE ADDED", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,{
					sound01 = Buffer.read(s,drop_01.string.replace("'",""););
				}
			);
		});

		add_02 = Button(ambiWindow)
		.states_([["ADD", Color.black, Color.white],["FILE ADDED", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,{
					sound02 = Buffer.read(s,drop_02.string.replace("'",""););
				}
			);
		});

		add_03 = Button(ambiWindow)
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
		rightLayout.add(playText = StaticText(ambiWindow));
		playText.string = "Then click on play";

		p01 = Button(ambiWindow, Rect(20, 20, 340, 30))
		.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					synth01 = Synth.new(\ambi_01, [\amp, v01.value,\buf, sound01.bufnum, \azi, sazi01.value.linlin(0,1,-180,180).round.neg, \ele, selev01.value.linlin(0,1,-90,90).round]).register;
				},{synth01.free}
			);
		});

		p02 = Button(ambiWindow, Rect(20, 20, 340, 30))
		.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					synth02 = Synth.new(\ambi_02, [\amp, v02.value,\buf, sound02.bufnum, \azi, sazi02.value.linlin(0,1,-180,180).round.neg, \ele, selev02.value.linlin(0,1,-90,90).round]).register;
				},{synth02.free}
			);
		});

		p03 = Button(ambiWindow, Rect(20, 20, 340, 30))
		.states_([["PLAY", Color.black, Color.white],["PAUSE", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					synth03 = Synth.new(\ambi_03, [\amp, v03.value,\buf, sound03.bufnum, \azi, sazi03.value.linlin(0,1,-180,180).round.neg, \ele, selev03.value.linlin(0,1,-90,90).round]).register;
				},{synth03.free}
			);
		});

		pAll = Button(ambiWindow)
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
		v.add(slidersLayout = HLayout());
		slidersLayout.add(source01Layout = VLayout(), 2, \left);
		slidersLayout.add(source02Layout = VLayout(), 2, \center);
		slidersLayout.add(source03Layout = VLayout(), 2, \right);


		//SLIDERS FOR AZIMUTH

		//SOURCE 1
		source01Layout.add(tazi01 = StaticText(ambiWindow));
		tazi01.string = "Azimuth angle [180, -180]";

		source01Layout.add(sazi01 = Slider());
		sazi01.orientation = \horizontal;
		//sazi01.Color(1,0.5,0);

		sazi01.action_({
			arg obj;
			var angle;
			angle = obj.value.linlin(0,1,-180,180).round.neg;
			tazi01.string = "Azimuth angle [180, -180] = "++ angle;

			if(
				synth01.isPlaying,{synth01.set(\azi, angle)}
			);
		});


		//SOURCE 2
		source02Layout.add(tazi02 = StaticText(ambiWindow));
		tazi02.string = "Azimuth angle [180, -180]";

		source02Layout.add(sazi02 = Slider());
		sazi02.orientation = \horizontal;

		sazi02.action_({
			arg obj;
			var angle;
			angle = obj.value.linlin(0,1,-180,180).round.neg;
			tazi02.string = "Azimuth angle [180, -180] = "++ angle;

			if(
				synth02.isPlaying,{synth02.set(\azi, angle)}
			);
		});


		//SOURCE 3
		source03Layout.add(tazi03 = StaticText(ambiWindow));
		tazi03.string = "Azimuth angle [180, -180]";

		source03Layout.add(sazi03 = Slider());
		sazi03.orientation = \horizontal;

		sazi03.action_({
			arg obj;
			var angle;
			angle = obj.value.linlin(0,1,-180,180).round.neg;
			tazi03.string = "Azimuth angle [180, -180] = "++ angle;


			if(
				synth03.isPlaying,{synth03.set(\azi, angle)}
			);
		});

		sazi01.background = Color(1,0.5,0);
		sazi02.background = Color(0,0.6,0.6);
		sazi03.background = Color(0.3,0.6,0);


		//SLIDER FOR ELEVATION

		//SOURCE 1
		source01Layout.add(telev01 = StaticText(ambiWindow));
		telev01.string = "Elevation angle [-90, 90]";

		source01Layout.add(selev01 = Slider());
		selev01.orientation = \horizontal;

		selev01.action_({
			arg obj;
			var elev;
			elev = obj.value.linlin(0,1,-90,90).round;

			telev01.string = "Elevation angle [-90, 90] = "++ elev;
			if(
				synth01.isPlaying,{synth01.set(\ele, elev)}
			);
		});


		//SOURCE 2
		source02Layout.add(telev02 = StaticText(ambiWindow));
		telev02.string = "Elevation angle [-90, 90]";

		source02Layout.add(selev02 = Slider());
		selev02.orientation = \horizontal;

		selev02.action_({
			arg obj;
			var elev;
			elev = obj.value.linlin(0,1,-180,180).round;

			telev02.string = "Elevation angle [-90, 90] = "++ elev;
			if(
				synth02.isPlaying,{synth02.set(\ele, elev)}
			);
		});

		//SOURCE 3
		source03Layout.add(telev03 = StaticText(ambiWindow));
		telev03.string = "Elevation angle [-90, 90]";

		source03Layout.add(selev03 = Slider());
		selev03.orientation = \horizontal;

		selev03.action_({
			arg obj;
			var elev;
			elev = obj.value.linlin(0,1,-180,180).round;

			telev03.string = "Elevation angle [-90, 90] = "++ elev;
			if(
				synth03.isPlaying,{synth03.set(\ele, elev)}
			);
		});

		selev01.background = Color(1,0.5,0);
		selev02.background = Color(0,0.6,0.6);
		selev03.background = Color(0.3,0.6,0);



		//SLIDER FOR VOLUME

		//SOURCE 1
		source01Layout.add(tvol01 = StaticText(ambiWindow));
		tvol01.string = "Volume";

		source01Layout.add(v01 = Slider());
		v01.orientation = \horizontal;

		v01.action_({
			arg obj;
			var volume;
			volume = obj.value;
			tvol01.string = "Volume = "++ (volume*100).round;
			if(
				synth01.isPlaying,{synth01.set(\amp, volume)}
			);
		});

		//SOURCE 2
		source02Layout.add(tvol02 = StaticText(ambiWindow));
		tvol02.string = "Volume";

		source02Layout.add(v02 = Slider());
		v02.orientation = \horizontal;

		v02.action_({
			arg obj;
			var volume;
			volume = obj.value;
			tvol02.string = "Volume = "++ (volume*100).round;
			if(
				synth02.isPlaying,{synth02.set(\amp, volume)}
			);
		});

		//SOURCE 3
		source03Layout.add(tvol03 = StaticText(ambiWindow));
		tvol03.string = "Volume";

		source03Layout.add(v03 = Slider());
		v03.orientation = \horizontal;

		v03.action_({
			arg obj;
			var volume;
			volume = obj.value;
			tvol03.string = "Volume = "++ (volume*100).round;
			if(
				synth03.isPlaying,{synth03.set(\amp, volume)}
			);
		});

		v01.background = Color(1,0.5,0);
		v02.background = Color(0,0.6,0.6);
		v03.background = Color(0.3,0.6,0);
	}
}
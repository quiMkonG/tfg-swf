SpatialAudio{
	classvar initWindow;

	*new{
		^super.new.init();
	}

	init{
		var v, h;
		var vbap_button, ambisonics_button, swf_button;
		var interface_text;


		initWindow = Window.new("Spatialization Tools", Rect(
			Window.screenBounds.width/2-100,
			Window.screenBounds.height/2-50,
			200,
			100
		)).front;
		initWindow.view.background_(Color.grey(0.8));
		initWindow.view.decorator = FlowLayout(initWindow.view.bounds);
		h = HLayout();
		v = VLayout(h);
		initWindow.layout =  v;
		initWindow.front;

		interface_text = StaticText(initWindow);
		interface_text.string = "Choose the spatialization tool";

		vbap_button = Button(initWindow)
		.states_([["VBAP", Color.black, Color.white],["VBAP CHOSEN", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					this.startVBAP;//començar una classe de vbap interface
				},{}
			);
		});

		ambisonics_button = Button(initWindow)
		.states_([["AMBISONICS", Color.black, Color.white],["AMBISONICS CHOSEN", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					this.startAmbi;//començar una classe de ambisonics interface
				},{}
			);
		});

		swf_button = Button(initWindow)
		.states_([["Sound Wavelets", Color.black, Color.white],["SWF CHOSEN", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					this.startSWF;//començar una classe de swf interface
				},{}
			);
		});

	}

	startVBAP{
		var vbap_lsp_window, vbap_interface;
		var v, h;
		var lsp, dim;
		var lsp_text;
		var lsp_stereo_button, lsp_9_6_button;

		vbap_lsp_window = Window.new("VBAP Configuration", Rect(
			Window.screenBounds.width/3-75,
			Window.screenBounds.height/2-50,
			200,
			100
		)).front;
		vbap_lsp_window.view.background_(Color.grey(0.8));
		vbap_lsp_window.view.decorator = FlowLayout(vbap_lsp_window.view.bounds);
		h = HLayout();
		v = VLayout(h);
		vbap_lsp_window.layout =  v;
		vbap_lsp_window.front;


		lsp_text = StaticText(vbap_lsp_window);
		lsp_text.string = "Choose your loudspeaker layout to use VBAP";

		lsp_stereo_button = Button(vbap_lsp_window)
		.states_([["STEREO", Color.black, Color.white],["STEREO CHOSEN", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					lsp = [30, -30];
					dim = 2;
					vbap_lsp_window.close;
					vbap_interface = VBAPInterface.new(dim, lsp);
				},{}
			);
		});

		lsp_9_6_button = Button(vbap_lsp_window)
		.states_([["9.6", Color.black, Color.white],["9.6 CHOSEN", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					lsp = [[40,0],[-40,0],[80,0],[-80,0],[120,0],[-120,0],[160,0],[-160,0],[0,0],[24,21],[-24,21],[90,26],[-90,30],[154,24.5],[-155,24.5],[20,-12]];
					dim = 3;
					vbap_lsp_window.close;
					vbap_interface = VBAPInterface.new(dim, lsp);
				},{}
			);
		});

	}

	startAmbi{
		var ambi_lsp_window, ambi_interface;
		var v, h;
		var lsp_text;
		var first_order_button, third_order_button;

		ambi_lsp_window = Window.new("Ambisonics Configuration", Rect(
			Window.screenBounds.width/3-75,
			Window.screenBounds.height/2-50,
			200,
			150
		)).front;
		ambi_lsp_window.view.background_(Color.grey(0.8));
		ambi_lsp_window.view.decorator = FlowLayout(ambi_lsp_window.view.bounds);
		h = HLayout();
		v = VLayout(h);
		ambi_lsp_window.layout =  v;
		ambi_lsp_window.front;

		lsp_text = StaticText(ambi_lsp_window);
		lsp_text.string = "Ambisonics is available in the 9.6 loudspeaker layout. Choose which order you want to use";

		first_order_button = Button(ambi_lsp_window)
		.states_([["1st Order Ambisonics", Color.black, Color.white],["1st Order Chosen", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					ambi_interface = AmbiInterface.new(1);//passem directament a ambi interface
				},{}
			);
		});

		third_order_button = Button(ambi_lsp_window)
		.states_([["3rd Order Ambisonics", Color.black, Color.white],["3rd Order Chosen", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					ambi_interface = AmbiInterface.new(3);//passem directament a ambi interface
				},{}
			);
		});

	}

	startSWF{
		var swf_level_window, swf_interface, v, h;
		var level_text;
		var level0_button, level1_button;

		swf_level_window = Window.new("SWF Level", Rect(
			Window.screenBounds.width/3-75,
			Window.screenBounds.height/2-50,
			200,
			150
		)).front;
		swf_level_window.view.background_(Color.grey(0.8));
		swf_level_window.view.decorator = FlowLayout(swf_level_window.view.bounds);
		h = HLayout();
		v = VLayout(h);
		swf_level_window.layout =  v;
		swf_level_window.front;

		level_text = StaticText(swf_level_window);
		level_text.string = "SWF is available in the 9.6 loudspeaker layout. Choose which level of wavelets you want to use";


		level0_button = Button(swf_level_window)
		.states_([["Wavelets at level 0", Color.black, Color.white],["Level 0 chosen", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					swf_interface = SWFInterface.new(0);//passem directament a ambi interface
				},{}
			);
		});

		level1_button = Button(swf_level_window)
		.states_([["Wavelets at level 1", Color.black, Color.white],["Level 1 chosen", Color.black, Color.white]])
		.action_({
			arg obj;
			if(
				obj.value == 1,
				{
					swf_interface = SWFInterface.new(1);//passem directament a ambi interface
				},{}
			);
		});

	}
}

/*

*/

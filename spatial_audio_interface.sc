SpatialAudio{
	classvar initWindow, lsp, dim;

	*new{
		^super.new.init();
	}

	init{
		var v, h;
		var vbap_button, ambisonics_button, swf_button;
		var interface_text;
		var start_vbap;

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
					start_vbap = this.startVBAP;//començar una classe de vbap interface
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
					//començar una classe de ambisonics interface
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
					//començar una classe de swf interface
				},{}
			);
		});

	}

	startVBAP{
		var vbap_lsp_window, vbap_interface;
		var v, h;
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
					lsp = [-30, 30];
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
}

/*

*/

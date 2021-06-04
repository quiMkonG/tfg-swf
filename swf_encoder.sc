SWFEncoder{
	classvar <vertices, <triplet_indices, <x_coordinates, <y_coordinates, <z_coordinates, <inverse_matrices;

	*new {
		^super.new.init();
	}

	//declare global variables, which are the same no matter the source localization we are encoding
	init {
		vertices = this.loadMatrix("/swf/swf-master/swf-interp_matrices/vertices_2.txt");
		triplet_indices = this.loadMatrix("/swf/triplet_indices.txt").asInteger;

		x_coordinates = this.getCoordinates(0);
		y_coordinates = this.getCoordinates(1);
		z_coordinates = this.getCoordinates(2);
		inverse_matrices = this.getInvMatrices();

    }

	//load matrices files from the library
	loadMatrix{|directory|
		var file, matrix;
		file = FileReader.read(Platform.userExtensionDir ++ directory);
		matrix = Array.fill2D(file.size, file[0].size,{
			arg r, c;
			file[r][c].asFloat;
		});
		^matrix;
	}

	//sorted arrays of triplet coordinates according to their indices
	getCoordinates{|index|
		var pointer_01, pointer_02, pointer_03, result;

		result = Array.fill(triplet_indices.size,{
			arg i;
			pointer_01 = triplet_indices[i][0].asInteger;//first triplet index
			pointer_02 = triplet_indices[i][1].asInteger;//second triplet index
			pointer_03 = triplet_indices[i][2].asInteger;//third triplet index
			//store their x/y/z coordinates
			[vertices[index][pointer_01],vertices[index][pointer_02],vertices[index][pointer_03]];
		});
		^result;
	}

	//compute inverse matrices for each triplet
	getInvMatrices{
		var inverse_matrices;
		inverse_matrices = Array2D.new(triplet_indices.size,9);

		for(0, triplet_indices.size-1,
			{arg i;
				var invmx, invdet;
				var lp1x, lp2x, lp3x, lp1y, lp2y, lp3y, lp1z, lp2z, lp3z;
				invmx = FloatArray.newClear(9);
				lp1x = x_coordinates[i][0];
				lp2x = x_coordinates[i][1];
				lp3x = x_coordinates[i][2];

				lp1y = y_coordinates[i][0];
				lp2y = y_coordinates[i][1];
				lp3y = y_coordinates[i][2];

				lp1z = z_coordinates[i][0];
				lp2z = z_coordinates[i][1];
				lp3z = z_coordinates[i][2];

				//"lp1x: % lp1y: % lp1z: % \n".postf(lp1x, lp1y, lp1z);
				//"lp2x: % lp2y: % lp2z: % \n",postf(lp2x, lp2y, lp2z);
				//"lp3x: % lp3y: % lp3z: % \n".postf(lp3x, lp3y, lp3z);

				invdet = 1.0 / (  (lp1x * ((lp2y * lp3z) - (lp2z * lp3y)))
					- (lp1y * ((lp2x * lp3z) - (lp2z * lp3x)))
					+ (lp1z * ((lp2x * lp3y) - (lp2y * lp3x))));


				invmx[0] = ((lp2y * lp3z) - (lp2z * lp3y)) * invdet;
				invmx[3] = ((lp1y * lp3z) - (lp1z * lp3y)) * invdet.neg;
				invmx[6] = ((lp1y * lp2z) - (lp1z * lp2y)) * invdet;
				invmx[1] = ((lp2x * lp3z) - (lp2z * lp3x)) * invdet.neg;
				invmx[4] = ((lp1x * lp3z) - (lp1z * lp3x)) * invdet;
				invmx[7] = ((lp1x * lp2z) - (lp1z * lp2x)) * invdet.neg;
				invmx[2] = ((lp2x * lp3y) - (lp2y * lp3x)) * invdet;
				invmx[5] = ((lp1x * lp3y) - (lp1y * lp3x)) * invdet.neg;
				invmx[8] = ((lp1x * lp2y) - (lp1y * lp2x)) * invdet;

				for(0,invmx.size-1,{arg j; inverse_matrices[i,j] = invmx[j]});
		});
		^inverse_matrices;
	}

	//convert angles to cartesian coordinates
	polarToCartesian{|azi, ele|
		var atorad = (2 * 3.1415927 / 360);
		var point = Array.newClear(3);
		point[0] = cos(azi * atorad) * cos(ele * atorad);
		point[1] = sin(azi * atorad) * cos(ele * atorad);
		point[2] = sin(ele * atorad);
		^point;
	}

	//gain computation according to vbap algorithm
	getGains{|azi, ele|
		var src_location;
		var temporal_gains, gains, channel_gains, power;
		var best_neg_g_am, big_sm_g, winner_set;

		src_location = this.polarToCartesian(azi, ele); //convert azimuth and elevation to cartesian coords.

		big_sm_g = -100000.0;	//initial value for largest minimum gain value
		best_neg_g_am = 3;		  // how many negative values in this set

		gains = Array.newClear(3);
		temporal_gains = Array.newClear(3);

		for(0, triplet_indices.size-1,//for each triplet
			{arg i; var small_g = 10000000.0, neg_g_am = 3, inv_pointer;
				for(0,2,//for each loudspeaker in triplet
					{arg j;
						temporal_gains[j] = 0.0;
						for(0,2,
							{arg k; var pointer;//auxiliar loop to compute each lspk gain
								pointer =  3*j + k;
								temporal_gains[j] = temporal_gains[j] + (src_location[k]*inverse_matrices[i, pointer]);
						});
						if(temporal_gains[j] < small_g,{small_g = temporal_gains[j];},{});
						if(temporal_gains[j] >= -0.01,{neg_g_am = neg_g_am - 1;},{});
				});

				if((small_g > big_sm_g) && (neg_g_am <= best_neg_g_am),
					{
						//"small_g: % big_sm_g: % neg_g_am: % best_neg_g_am: % \n".postf(small_g, big_sm_g, neg_g_am, best_neg_g_am);
						big_sm_g = small_g;
						best_neg_g_am = neg_g_am;
						winner_set=i;
						gains[0] = temporal_gains[0];
						gains[1] = temporal_gains[1];
						gains[2] = temporal_gains[2];
						//winner_set.postln;
						//gains.postln;
				},{});
		});
		power =((gains[0]**2) + (gains[1]**2) + (gains[2]**2)).sqrt;
		gains[0] = gains[0]/power;
		gains[1] = gains[1]/power;
		gains[2] = gains[2]/power;

		channel_gains = FloatArray.fill(66,{0});
		for(0,2,{
			arg i; var aux;
			aux = triplet_indices[winner_set][i].asInteger;
			channel_gains[aux] = gains[i];
		});
		^channel_gains;
	}

	matmul{|mat1, mat2|
		var result;

		//if(mat1[0].size != mat2[0].size,{"WARNING: matrix dimensions are wrong".postln; result = 0; ^result},{});

		result = Array.fill(mat2[0].size-1,{
			arg i;var sum = 0;
			for(0,mat1.size-1,{
				arg j;
				sum = sum + (mat1[j] * mat2[j][i]);
			});
			sum;
		});
		^result;
	}
/*
	getWaveletTransform{|channel_gains|
		var swf_transform;
		var c0, c1, d0, d1;
		var matrix_A1, matrix_A0, matrix_B1, matrix_B0;

		matrix_A1 =
		matrix_A0 =
		matrix_B1 =
		matrix_B0 =

		swf_transform = matmul(channel_gains, )
	}*/
}
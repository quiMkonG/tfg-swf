(
//SWF variables
var vertices2, triplet_indices;
var matrix_A1, matrix_A0, matrix_B1, matrix_B0, matrix_Q0, matrix_Q1, matrix_P0, matrix_P1;
var coarse_01, coarse_00, details_01, details_00;
var dec_c1, dec_f;
var swf_transform;

//VBAP variables
var x_coordinates, y_coordinates, z_coordinates;
var inverse_matrices;
var atorad = (2 * 3.1415927 / 360);
var p0,azi, ele;
var temporal_gains, best_neg_g_am, big_sm_g, gains, power, winner_set;
var channel_gains;


"checkpoint".postln;

//LOAD SWF MATRICES
f = FileReader.read("C:/Users/usuario/AppData/Local/SuperCollider/Extensions/swf/swf-master/swf-interp_matrices/vertices_2.txt");//"C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/vertices_2.txt"
vertices2 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/usuario/AppData/Local/SuperCollider/Extensions/swf/triplet_indices.txt");//"C:/Users/quimm/Desktop/tfg-swf-main/triplet_indices.txt"
triplet_indices = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});
)
f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_A_1.txt").flop;
matrix_A1 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_A_0.txt").flop;
matrix_A0 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_B_1.txt").flop;
matrix_B1 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_B_0.txt").flop;
matrix_B0 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_P_1.txt").flop;
matrix_P1 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_P_0.txt").flop;
matrix_P0 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});


f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_Q_1.txt").flop;
matrix_Q1 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});

f = FileReader.read("C:/Users/quimm/Desktop/swf-master/swf-interp_matrices/mat_Q_0.txt").flop;
matrix_Q0 = Array.fill2D(f.size, f[0].size,{
	arg r, c;
	f[r][c].asFloat;
});



/*
VBAP PART:
Mesh vertices are considered as VBAP loudspeakers in order to interpolate
channel gains according vertices triplets
*/

//LOAD TRIPLET COORDINATES INTO AN ARRAY FOR EACH AXIS
x_coordinates = Array.fill(triplet_indices.size,
	{arg i; var pointer_01, pointer_02, pointer_03;
		pointer_01 = triplet_indices[i][0].asInteger;//first triplet index
		pointer_02 = triplet_indices[i][1].asInteger;//second triplet index
		pointer_03 = triplet_indices[i][2].asInteger;//third triplet index
		//store their x coordinates
		[vertices2[0][pointer_01],vertices2[0][pointer_02],vertices2[0][pointer_03]];
	});

y_coordinates = Array.fill(triplet_indices.size,
	{arg i; var pointer_01, pointer_02, pointer_03;
		pointer_01 = triplet_indices[i][0].asInteger;//first triplet index
		pointer_02 = triplet_indices[i][1].asInteger;//second triplet index
		pointer_03 = triplet_indices[i][2].asInteger;//third triplet index
		//store their y coordinates
		[vertices2[1][pointer_01],vertices2[1][pointer_02],vertices2[1][pointer_03]];
	});

z_coordinates = Array.fill(triplet_indices.size,
	{arg i; var pointer_01, pointer_02, pointer_03;
		pointer_01 = triplet_indices[i][0].asInteger;//first triplet index
		pointer_02 = triplet_indices[i][1].asInteger;//second triplet index
		pointer_03 = triplet_indices[i][2].asInteger;//third triplet index
		//store their z coordinates
		[vertices2[2][pointer_01],vertices2[2][pointer_02],vertices2[2][pointer_03]];
	});


//CALCULATE INVERSE MATRICES FOR EACH TRIPLET

inverse_matrices = Array2D.new(triplet_indices.size,9);

for(0, triplet_indices.size-1,
	{arg i; var invmx, invdet, lp1x, lp2x, lp3x, lp1y, lp2y, lp3y, lp1z, lp2z, lp3z;
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

		//"lp1x: % lp1y: % lp1z: % \nlp2x: % lp2y: % lp2z: % \nlp3x: % lp3y: % lp3z: % \n".postf(lp1x, lp1y, lp1z, lp2x, lp2y, lp2z, lp3x, lp3y, lp3z);


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

//SOUND SOURCE POSITION

azi = 0;
ele = 0;

p0 = Array.newClear(3);
p0[0] = cos(azi * atorad) * cos(ele * atorad);
p0[1] = sin(azi * atorad) * cos(ele * atorad);
p0[2] = sin(ele * atorad);

"x: % y: % z: % \n".postf(p0[0], p0[1], p0[2]);


//FIND TRIPLET CORRESPONDING TO THE SOUND SOURCE POSITION
//AND CALCULATE GAINS FOR EACH LSPK IN THE TRIPLET

big_sm_g = -100000.0;	/* initial value for largest minimum gain value */
best_neg_g_am = 3;		  /* how many negative values in this set */

gains = Array.newClear(3);
temporal_gains = Array.newClear(3);

for(0, triplet_indices.size-1,//for each triplet in coordinates 0:127)
	{arg i; var small_g = 10000000.0, neg_g_am = 3, inv_pointer;
		for(0,2,//run through dimensions
			{arg j;
				temporal_gains[j] = 0.0;
				for(0,2,
					{arg k; var pointer;
						pointer =  3*j + k;
						//if(i==1)
						temporal_gains[j] = temporal_gains[j] + (p0[k]*inverse_matrices[i, pointer]);
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
				winner_set.postln;
				gains[0] = temporal_gains[0];
				gains[1] = temporal_gains[1];
				gains[2] = temporal_gains[2];
				gains.postln;
		},{});
});
power =((gains[0]**2) + (gains[1]**2) + (gains[2]**2)).sqrt;
gains[0] = gains[0]/power;
gains[1] = gains[1]/power;
gains[2] = gains[2]/power;


//STORE TRIPLET GAIN VALUES IN THE CORRESPONING CHANNEL OF THE MESH

channel_gains = FloatArray.fill(66,{0});
for(0,2,{
	arg i; var aux;
	aux = triplet_indices[winner_set][i].asInteger;
	channel_gains[aux] = gains[i];
});
//channel_gains.postln;

//SWF PART


coarse_01 = Array.fill(18,{
	arg i;var sum = 0;
	for(0,channel_gains.size-1,{
		arg j;
		sum = sum + (channel_gains[j] * matrix_A1[j][i]);
	});
	sum;
});

details_01 = Array.fill(48,{
	arg i; var sum = 0;
	for(0,channel_gains.size-1,{
		arg j;
		sum = sum + (channel_gains[j] * matrix_B1[j][i]);
	});
	sum;
});

coarse_00 = Array.fill(6,{
	arg i;var sum = 0;
	for(0,coarse_01.size-1,{
		arg j;
		sum = sum + (coarse_01[j] * matrix_A0[j][i]);
	});
	sum;
});

details_00 = Array.fill(12,{
	arg i;var sum = 0;
	for(0,coarse_01.size-1,{
		arg j;
		sum = sum + (coarse_01[j] * matrix_B0[j][i]);
	});
	sum;
});

swf_transform = Array.with(coarse_00, details_00, details_01);
//swf_transform.postln;

dec_c1 = Array.fill(18,{
	arg i;var sum_c0 = 0, sum_d0 = 0;
	for(0,matrix_P0.size-1,{
		arg j;
		sum_c0 = sum_c0 + (coarse_00[j] * matrix_P0[j][i]);
	});

	for(0, matrix_Q0.size-1,{
		arg k;
		sum_d0 = sum_d0 + (details_00[k] * matrix_Q0[k][i]);
	});
	sum_c0 + sum_d0;
});

dec_f = Array.fill(66,{
	arg i;var sum_c1 = 0, sum_d1 = 0;
	for(0,matrix_P1.size-1,{
		arg j;
		sum_c1 = sum_c1 + (dec_c1[j] * matrix_P1[j][i]);
	});

	for(0, matrix_Q1.size-1,{
		arg k;
		sum_d1 = sum_d1 + (details_01[k] * matrix_Q1[k][i]);
	});
	sum_c1 + sum_d1;
});
)
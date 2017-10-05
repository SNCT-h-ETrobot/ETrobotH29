package Information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockArrangeInfo {
	static private final int GREEN_PLACE_ID = 0; //仮の番号。当日打ち込む
	static private final int K_BLACK = 1331; //11*11*11
	static private final int K_RED = 121; //11*11
	static private final int K_YELLOW = 11; //11
	static private final int K_BLUE = 1;
	static private int[] blockPlaceIDList = new int[5];
	static private Map<Integer,Map<Vertex,Path>> connectionList = new HashMap<Integer,Map<Vertex,Path>>();
	static private List<Object> pointList = new ArrayList<Object>();
	static private List<BlockPlace> blockPlaceList = new ArrayList<BlockPlace>();

	static public BlockPlace b000 = new BlockPlace(0,0.000F,0.000F,1);
	static public BlockPlace b004 = new BlockPlace(4,77.942F,0.000F,3);
	static public BlockPlace b008 = new BlockPlace(8,155.884F,0.000F,4);
	static public BlockPlace b012 = new BlockPlace(12,233.826F,0.000F,3);
	static public BlockPlace b044 = new BlockPlace(44,38.971F,22.500F,4);
	static public BlockPlace b052 = new BlockPlace(52,116.913F,22.500F,2);
	static public BlockPlace b060 = new BlockPlace(60,194.855F,22.500F,1);
	static public BlockPlace b074 = new BlockPlace(74,77.942F,45.000F,1);
	static public BlockPlace b081 = new BlockPlace(81,155.884F,45.000F,3);
	static public BlockPlace b090 = new BlockPlace(90,16.471F,61.472F,2);
	static public BlockPlace b119 = new BlockPlace(119,217.355F,61.472F,2);
	static public BlockPlace b121 = new BlockPlace(121,55.442F,83.972F,3);
	static public BlockPlace b128 = new BlockPlace(128,100.442F,83.972F,4);
	static public BlockPlace b132 = new BlockPlace(132,133.384F,83.972F,1);
	static public BlockPlace b139 = new BlockPlace(139,178.384F,83.972F,4);

	static public VirtualVertex v002 = new VirtualVertex(2,38.971F,0.000F,true);
	static public VirtualVertex v006 = new VirtualVertex(6,116.913F,0.000F,true);
	static public VirtualVertex v010 = new VirtualVertex(10,194.855F,0.000F,true);
	static public VirtualVertex v015 = new VirtualVertex(15,19.486F,11.250F,true);
	static public VirtualVertex v018 = new VirtualVertex(18,38.971F,7.500F,false);
	static public VirtualVertex v020 = new VirtualVertex(20,58.457F,11.250F,true);
	static public VirtualVertex v023 = new VirtualVertex(23,97.428F,11.250F,true);
	static public VirtualVertex v026 = new VirtualVertex(26,116.913F,7.500F,false);
	static public VirtualVertex v028 = new VirtualVertex(28,136.399F,11.250F,true);
	static public VirtualVertex v031 = new VirtualVertex(31,175.370F,11.250F,true);
	static public VirtualVertex v034 = new VirtualVertex(34,194.855F,7.500F,false);
	static public VirtualVertex v036 = new VirtualVertex(36,214.341F,11.250F,true);
	static public VirtualVertex v039 = new VirtualVertex(39,8.236F,30.736F,true);
	static public VirtualVertex v042 = new VirtualVertex(42,18.481F,27.991F,false);
	static public VirtualVertex v048 = new VirtualVertex(48,77.942F,22.500F,false);
	static public VirtualVertex v056 = new VirtualVertex(56,155.884F,22.500F,false);
	static public VirtualVertex v062 = new VirtualVertex(62,224.586F,27.991F,false);
	static public VirtualVertex v065 = new VirtualVertex(65,225.590F,30.736F,true);
	static public VirtualVertex v068 = new VirtualVertex(68,27.721F,41.986F,true);
	static public VirtualVertex v071 = new VirtualVertex(71,58.457F,33.750F,true);
	static public VirtualVertex v077 = new VirtualVertex(77,97.428F,33.750F,true);
	static public VirtualVertex v078 = new VirtualVertex(78,136.399F,33.750F,true);
	static public VirtualVertex v084 = new VirtualVertex(84,175.370F,33.750F,true);
	static public VirtualVertex v087 = new VirtualVertex(87,206.105F,41.986F,true);
	static public VirtualVertex v093 = new VirtualVertex(93,35.957F,72.722F,true);
	static public VirtualVertex v095 = new VirtualVertex(95,47.207F,53.236F,false);
	static public VirtualVertex v098 = new VirtualVertex(98,66.692F,64.486F,true);
	static public VirtualVertex v101 = new VirtualVertex(101,77.942F,70.981F,false);
	static public VirtualVertex v104 = new VirtualVertex(104,89.192F,64.486F,true);
	static public VirtualVertex v105 = new VirtualVertex(105,144.634F,64.486F,true);
	static public VirtualVertex v108 = new VirtualVertex(108,155.884F,70.981F,false);
	static public VirtualVertex v111 = new VirtualVertex(111,167.134F,64.486F,true);
	static public VirtualVertex v114 = new VirtualVertex(114,186.620F,53.236F,false);
	static public VirtualVertex v116 = new VirtualVertex(116,197.870F,72.722F,true);
	static public VirtualVertex v124 = new VirtualVertex(124,77.942F,83.972F,true);
	static public VirtualVertex v130 = new VirtualVertex(130,116.913F,83.972F,true);
	static public VirtualVertex v136 = new VirtualVertex(136,155.884F,83.972F,true);
	static public VirtualVertex v151 = new VirtualVertex(151,116.913F,53.236F,false);

	static public Path p001 = new Path(1,38.971F,0.000F,true);
	static public Path p003 = new Path(3,38.971F,0.000F,true);
	static public Path p005 = new Path(5,38.971F,0.000F,true);
	static public Path p007 = new Path(7,38.971F,0.000F,true);
	static public Path p009 = new Path(9,38.971F,0.000F,true);
	static public Path p011 = new Path(11,38.971F,0.000F,true);
	static public Path p013 = new Path(13,31.812F,75.000F,true);
	static public Path p014 = new Path(14,22.500F,30.000F,true);
	static public Path p016 = new Path(16,19.843F,345.000F,false);
	static public Path p017 = new Path(17,7.5F,90.000F,false);
	static public Path p019 = new Path(19,19.843F,15.000F,false);
	static public Path p021 = new Path(21,22.500F,150.000F,true);
	static public Path p022 = new Path(22,22.500F,30.000F,true);
	static public Path p024 = new Path(24,19.843F,345.000F,false);
	static public Path p025 = new Path(25,7.500F,90.000F,false);
	static public Path p027 = new Path(27,19.843F,15.000F,false);
	static public Path p029 = new Path(29,22.500F,150.000F,true);
	static public Path p030 = new Path(30,22.500F,30.000F,true);
	static public Path p032 = new Path(32,19.843F,345.000F,false);
	static public Path p033 = new Path(33,7.500F,90.000F,false);
	static public Path p035 = new Path(35,19.843F,15.000F,false);
	static public Path p037 = new Path(37,22.500F,150.000F,true);
	static public Path p038 = new Path(38,31.812F,105.000F,true);
	static public Path p040 = new Path(40,10.606F,345.000F,false);
	static public Path p041 = new Path(41,16.771F,97.500F,false);
	static public Path p043 = new Path(43,22.500F,30.000F,true);
	static public Path p045 = new Path(45,22.500F,150.000F,true);
	static public Path p046 = new Path(46,22.500F,30.000F,true);
	static public Path p047 = new Path(47,22.500F,30.000F,false);
	static public Path p049 = new Path(49,22.500F,150.000F,false);
	static public Path p050 = new Path(50,22.500F,30.000F,true);
	static public Path p051 = new Path(51,22.500F,150.000F,true);
	static public Path p053 = new Path(53,22.500F,150.000F,true);
	static public Path p054 = new Path(54,22.500F,30.000F,true);
	static public Path p055 = new Path(55,22.500F,30.000F,false);
	static public Path p057 = new Path(57,22.500F,150.000F,false);
	static public Path p058 = new Path(58,22.500F,30.000F,true);
	static public Path p059 = new Path(59,22.500F,150.000F,true);
	static public Path p061 = new Path(61,22.500F,150.000F,true);
	static public Path p063 = new Path(63,16.771F,82.5F,false);
	static public Path p064 = new Path(64,10.606F,15.000F,false);
	static public Path p066 = new Path(66,31.812F,75.000F,true);
	static public Path p067 = new Path(67,16.770F,52.500F,false);
	static public Path p069 = new Path(69,22.500F,120.000F,true);
	static public Path p070 = new Path(70,22.500F,30.000F,false);
	static public Path p072 = new Path(72,22.500F,150.000F,false);
	static public Path p073 = new Path(73,22.500F,30.000F,true);
	static public Path p075 = new Path(75,22.500F,30.000F,false);
	static public Path p076 = new Path(76,22.500F,330.000F,true);
	static public Path p079 = new Path(79,22.500F,150.000F,false);
	static public Path p080 = new Path(80,22.500F,30.000F,true);
	static public Path p082 = new Path(82,22.500F,30.000F,false);
	static public Path p083 = new Path(83,22.500F,330.000F,true);
	static public Path p085 = new Path(85,22.500F,60.000F,true);
	static public Path p086 = new Path(86,22.500F,150.000F,false);
	static public Path p088 = new Path(88,16.77F,127.500F,false);
	static public Path p089 = new Path(89,31.812F,105.000F,true);
	static public Path p091 = new Path(91,22.500F,120.000F,true);
	static public Path p092 = new Path(92,22.500F,30.000F,true);
	static public Path p094 = new Path(94,22.500F,300.000F,false);
	static public Path p096 = new Path(96,22.500F,120.000F,false);
	static public Path p097 = new Path(97,22.500F,30.000F,false);
	static public Path p099 = new Path(99,22.500F,120.000F,true);
	static public Path p100 = new Path(100,12.990F,30.000F,false);
	static public Path p102 = new Path(102,12.990F,330.000F,false);
	static public Path p103 = new Path(103,22.500F,60.000F,true);
	static public Path p106 = new Path(106,22.500F,120.000F,true);
	static public Path p107 = new Path(107,12.990F,30.000F,false);
	static public Path p109 = new Path(109,12.990F,330.000F,false);
	static public Path p110 = new Path(110,22.500F,60.000F,true);
	static public Path p112 = new Path(112,22.500F,330.000F,false);
	static public Path p113 = new Path(113,22.500F,60.000F,false);
	static public Path p115 = new Path(115,22.500F,60.000F,false);
	static public Path p117 = new Path(117,22.500F,330.000F,true);
	static public Path p118 = new Path(118,22.500F,60.000F,true);
	static public Path p120 = new Path(120,22.500F,30.000F,true);
	static public Path p122 = new Path(122,22.500F,120.000F,true);
	static public Path p123 = new Path(123,22.500F,0.000F,true);
	static public Path p125 = new Path(125,12.991F,90.000F,false);
	static public Path p126 = new Path(126,22.500F,0.000F,true);
	static public Path p127 = new Path(127,22.500F,60.000F,true);
	static public Path p129 = new Path(129,16.471F,0.000F,true);
	static public Path p131 = new Path(131,16.471F,0.000F,true);
	static public Path p133 = new Path(133,22.500F,120.000F,true);
	static public Path p134 = new Path(134,22.500F,0.000F,true);
	static public Path p135 = new Path(135,12.991F,90.000F,false);
	static public Path p137 = new Path(137,22.500F,0.000F,true);
	static public Path p138 = new Path(138,22.500F,60.000F,true);
	static public Path p140 = new Path(140,22.500F,150.000F,true);
	static public Path p141 = new Path(141,27.557F,45.000F,false);
	static public Path p142 = new Path(142,39.835F,26.500F,false);
	static public Path p143 = new Path(143,29.917F,338.000F,false);
	static public Path p144 = new Path(144,34.871F,300.000F,false);
	static public Path p145 = new Path(145,30.736F,270.000F,false);
	static public Path p146 = new Path(146,34.871F,240.000F,false);
	static public Path p147 = new Path(147,29.917F,202.000F,false);
	static public Path p148 = new Path(148,39.835F,153.500F,false);
	static public Path p149 = new Path(149,27.557F,135.000F,false);
	static public Path p150 = new Path(150,30.736F,90.000F,false);

	public static void makeConnection(){
		connectionList.put(b000.getPointID(),new HashMap<Vertex,Path>());
		connectionList.get(b000.getPointID()).put(v002, p001);
		connectionList.get(b000.getPointID()).put(v015, p014);
		connectionList.get(b000.getPointID()).put(v039, p013);
		connectionList.put(v002.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v002.getPointID()).put(b000, p001);
		connectionList.get(v002.getPointID()).put(b004, p003);
		connectionList.get(v002.getPointID()).put(v018, p017);
		connectionList.put(b004.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b004.getPointID()).put(v002, p003);
		connectionList.get(b004.getPointID()).put(v006, p005);
		connectionList.get(b004.getPointID()).put(v020, p021);
		connectionList.get(b004.getPointID()).put(v023, p022);
		connectionList.put(v006.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v006.getPointID()).put(b004, p005);
		connectionList.get(v006.getPointID()).put(b008, p007);
		connectionList.get(v006.getPointID()).put(v026, p025);
		connectionList.put(b008.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b008.getPointID()).put(v006, p007);
		connectionList.get(b008.getPointID()).put(v010, p009);
		connectionList.get(b008.getPointID()).put(v028, p029);
		connectionList.get(b008.getPointID()).put(v031, p030);
		connectionList.put(v010.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v010.getPointID()).put(b008, p009);
		connectionList.get(v010.getPointID()).put(b012, p011);
		connectionList.get(v010.getPointID()).put(v034, p033);
		connectionList.put(b012.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b012.getPointID()).put(v010, p011);
		connectionList.get(b012.getPointID()).put(v036, p037);
		connectionList.get(b012.getPointID()).put(v065, p038);
		connectionList.put(v015.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v015.getPointID()).put(b000, p014);
		connectionList.get(v015.getPointID()).put(v018, p016);
		connectionList.get(v015.getPointID()).put(v042, p041);
		connectionList.get(v015.getPointID()).put(b044, p043);
		connectionList.put(v018.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v018.getPointID()).put(v002, p017);
		connectionList.get(v018.getPointID()).put(v015, p016);
		connectionList.get(v018.getPointID()).put(v020, p019);
		connectionList.put(v020.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v020.getPointID()).put(b004, p021);
		connectionList.get(v020.getPointID()).put(v018, p019);
		connectionList.get(v020.getPointID()).put(b044, p045);
		connectionList.get(v020.getPointID()).put(v048, p047);
		connectionList.put(v023.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v023.getPointID()).put(b004, p022);
		connectionList.get(v023.getPointID()).put(v026, p024);
		connectionList.get(v023.getPointID()).put(v048, p049);
		connectionList.get(v023.getPointID()).put(b052, p050);
		connectionList.put(v026.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v026.getPointID()).put(v006, p025);
		connectionList.get(v026.getPointID()).put(v023, p024);
		connectionList.get(v026.getPointID()).put(v028, p027);
		connectionList.put(v028.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v028.getPointID()).put(b008, p029);
		connectionList.get(v028.getPointID()).put(v026, p027);
		connectionList.get(v028.getPointID()).put(b052, p053);
		connectionList.get(v028.getPointID()).put(v056, p055);
		connectionList.put(v031.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v031.getPointID()).put(b008, p030);
		connectionList.get(v031.getPointID()).put(v034, p032);
		connectionList.get(v031.getPointID()).put(v056, p057);
		connectionList.get(v031.getPointID()).put(b060, p058);
		connectionList.put(v034.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v034.getPointID()).put(v010, p033);
		connectionList.get(v034.getPointID()).put(v031, p032);
		connectionList.get(v034.getPointID()).put(v036, p035);
		connectionList.put(v036.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v036.getPointID()).put(b012, p037);
		connectionList.get(v036.getPointID()).put(v034, p035);
		connectionList.get(v036.getPointID()).put(b060, p061);
		connectionList.get(v036.getPointID()).put(v062, p063);
		connectionList.put(v039.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v039.getPointID()).put(b000, p013);
		connectionList.get(v039.getPointID()).put(v042, p040);
		connectionList.get(v039.getPointID()).put(b090, p066);
		connectionList.put(v042.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v042.getPointID()).put(v015, p041);
		connectionList.get(v042.getPointID()).put(v039, p040);
		connectionList.get(v042.getPointID()).put(v068, p067);
		connectionList.put(b044.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b044.getPointID()).put(v015, p043);
		connectionList.get(b044.getPointID()).put(v020, p045);
		connectionList.get(b044.getPointID()).put(v068, p069);
		connectionList.get(b044.getPointID()).put(v071, p046);
		connectionList.put(v048.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v048.getPointID()).put(v020, p047);
		connectionList.get(v048.getPointID()).put(v023, p049);
		connectionList.get(v048.getPointID()).put(v071, p072);
		connectionList.get(v048.getPointID()).put(v077, p075);
		connectionList.put(b052.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b052.getPointID()).put(v023, p050);
		connectionList.get(b052.getPointID()).put(v028, p053);
		connectionList.get(b052.getPointID()).put(v077, p051);
		connectionList.get(b052.getPointID()).put(v078, p054);
		connectionList.get(b052.getPointID()).put(v151, p150);
		connectionList.put(v056.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v056.getPointID()).put(v028, p055);
		connectionList.get(v056.getPointID()).put(v031, p057);
		connectionList.get(v056.getPointID()).put(v078, p079);
		connectionList.get(v056.getPointID()).put(v084, p082);
		connectionList.put(b060.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b060.getPointID()).put(v031, p058);
		connectionList.get(b060.getPointID()).put(v036, p061);
		connectionList.get(b060.getPointID()).put(v084, p059);
		connectionList.get(b060.getPointID()).put(v087, p085);
		connectionList.put(v062.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v062.getPointID()).put(v036, p063);
		connectionList.get(v062.getPointID()).put(v065, p064);
		connectionList.get(v062.getPointID()).put(v087, p088);
		connectionList.put(v065.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v065.getPointID()).put(b012, p038);
		connectionList.get(v065.getPointID()).put(v062, p064);
		connectionList.get(v065.getPointID()).put(b119, p089);
		connectionList.put(v068.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v068.getPointID()).put(v042, p067);
		connectionList.get(v068.getPointID()).put(b044, p069);
		connectionList.get(v068.getPointID()).put(b090, p091);
		connectionList.get(v068.getPointID()).put(v095, p070);
		connectionList.put(v071.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v071.getPointID()).put(b044, p046);
		connectionList.get(v071.getPointID()).put(v048, p072);
		connectionList.get(v071.getPointID()).put(b074, p073);
		connectionList.get(v071.getPointID()).put(v095, p096);
		connectionList.put(b074.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b074.getPointID()).put(v071, p073);
		connectionList.get(b074.getPointID()).put(v077, p076);
		connectionList.get(b074.getPointID()).put(v098, p099);
		connectionList.get(b074.getPointID()).put(v104, p103);
		connectionList.get(b074.getPointID()).put(v151, p142);
		connectionList.put(v077.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v077.getPointID()).put(v048, p075);
		connectionList.get(v077.getPointID()).put(b052, p051);
		connectionList.get(v077.getPointID()).put(b074, p076);
		connectionList.get(v077.getPointID()).put(v151, p141);
		connectionList.put(v078.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v078.getPointID()).put(b052, p054);
		connectionList.get(v078.getPointID()).put(v056, p079);
		connectionList.get(v078.getPointID()).put(b081, p080);
		connectionList.get(v078.getPointID()).put(v151, p149);
		connectionList.put(b081.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b081.getPointID()).put(v078, p080);
		connectionList.get(b081.getPointID()).put(v084, p083);
		connectionList.get(b081.getPointID()).put(v105, p106);
		connectionList.get(b081.getPointID()).put(v111, p110);
		connectionList.get(b081.getPointID()).put(v151, p148);
		connectionList.put(v084.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v084.getPointID()).put(v056, p082);
		connectionList.get(v084.getPointID()).put(b060, p059);
		connectionList.get(v084.getPointID()).put(b081, p083);
		connectionList.get(v084.getPointID()).put(v114, p113);
		connectionList.put(v087.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v087.getPointID()).put(b060, p085);
		connectionList.get(v087.getPointID()).put(v062, p088);
		connectionList.get(v087.getPointID()).put(v114, p086);
		connectionList.get(v087.getPointID()).put(b119, p118);
		connectionList.put(b090.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b090.getPointID()).put(v039, p066);
		connectionList.get(b090.getPointID()).put(v068, p091);
		connectionList.get(b090.getPointID()).put(v093, p092);
		connectionList.put(v093.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v093.getPointID()).put(b090, p092);
		connectionList.get(v093.getPointID()).put(v095, p094);
		connectionList.get(v093.getPointID()).put(b121, p120);
		connectionList.put(v095.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v095.getPointID()).put(v068, p070);
		connectionList.get(v095.getPointID()).put(v071, p096);
		connectionList.get(v095.getPointID()).put(v093, p094);
		connectionList.get(v095.getPointID()).put(v098, p097);
		connectionList.put(v098.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v098.getPointID()).put(b074, p099);
		connectionList.get(v098.getPointID()).put(v095, p097);
		connectionList.get(v098.getPointID()).put(v101, p100);
		connectionList.get(v098.getPointID()).put(b121, p122);
		connectionList.put(v101.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v101.getPointID()).put(v098, p100);
		connectionList.get(v101.getPointID()).put(v104, p102);
		connectionList.get(v101.getPointID()).put(v124, p125);
		connectionList.put(v104.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v104.getPointID()).put(b074, p103);
		connectionList.get(v104.getPointID()).put(v101, p102);
		connectionList.get(v104.getPointID()).put(b128, p127);
		connectionList.get(v104.getPointID()).put(v151, p143);
		connectionList.put(v105.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v105.getPointID()).put(b081, p106);
		connectionList.get(v105.getPointID()).put(v108, p107);
		connectionList.get(v105.getPointID()).put(b132, p133);
		connectionList.get(v105.getPointID()).put(v151, p147);
		connectionList.put(v108.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v108.getPointID()).put(v105, p107);
		connectionList.get(v108.getPointID()).put(v111, p109);
		connectionList.get(v108.getPointID()).put(v136, p135);
		connectionList.put(v111.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v111.getPointID()).put(b081, p110);
		connectionList.get(v111.getPointID()).put(v108, p109);
		connectionList.get(v111.getPointID()).put(v114, p112);
		connectionList.get(v111.getPointID()).put(b139, p138);
		connectionList.put(v114.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v114.getPointID()).put(v084, p113);
		connectionList.get(v114.getPointID()).put(v087, p086);
		connectionList.get(v114.getPointID()).put(v111, p112);
		connectionList.get(v114.getPointID()).put(v116, p115);
		connectionList.put(v116.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v116.getPointID()).put(v114, p115);
		connectionList.get(v116.getPointID()).put(b119, p117);
		connectionList.get(v116.getPointID()).put(b139, p140);
		connectionList.put(b119.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b119.getPointID()).put(v065, p089);
		connectionList.get(b119.getPointID()).put(v087, p118);
		connectionList.get(b119.getPointID()).put(v116, p117);
		connectionList.put(b121.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b121.getPointID()).put(v093, p120);
		connectionList.get(b121.getPointID()).put(v098, p122);
		connectionList.get(b121.getPointID()).put(v124, p123);
		connectionList.put(v124.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v124.getPointID()).put(v101, p125);
		connectionList.get(v124.getPointID()).put(b121, p123);
		connectionList.get(v124.getPointID()).put(b128, p126);
		connectionList.put(b128.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b128.getPointID()).put(v104, p127);
		connectionList.get(b128.getPointID()).put(v124, p126);
		connectionList.get(b128.getPointID()).put(v130, p129);
		connectionList.get(b128.getPointID()).put(v151, p144);
		connectionList.put(v130.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v130.getPointID()).put(b128, p129);
		connectionList.get(v130.getPointID()).put(b132, p131);
		connectionList.get(v130.getPointID()).put(v151, p145);
		connectionList.put(b132.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b132.getPointID()).put(v105, p133);
		connectionList.get(b132.getPointID()).put(v130, p131);
		connectionList.get(b132.getPointID()).put(v136, p134);
		connectionList.get(b132.getPointID()).put(v151, p146);
		connectionList.put(v136.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v136.getPointID()).put(v108, p135);
		connectionList.get(v136.getPointID()).put(b132, p134);
		connectionList.get(v136.getPointID()).put(b139, p137);
		connectionList.put(b139.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(b139.getPointID()).put(v111, p138);
		connectionList.get(b139.getPointID()).put(v116, p140);
		connectionList.get(b139.getPointID()).put(v136, p137);
		connectionList.put(v151.getPointID(), new HashMap<Vertex,Path>());
		connectionList.get(v151.getPointID()).put(b052, p150);
		connectionList.get(v151.getPointID()).put(b074, p142);
		connectionList.get(v151.getPointID()).put(v077, p141);
		connectionList.get(v151.getPointID()).put(v078, p149);
		connectionList.get(v151.getPointID()).put(b081, p148);
		connectionList.get(v151.getPointID()).put(v104, p143);
		connectionList.get(v151.getPointID()).put(v105, p147);
		connectionList.get(v151.getPointID()).put(b128, p144);
		connectionList.get(v151.getPointID()).put(v130, p145);
		connectionList.get(v151.getPointID()).put(b132, p146);

		pointList.add(b000);
		pointList.add(p001);
		pointList.add(v002);
		pointList.add(p003);
		pointList.add(b004);
		pointList.add(p005);
		pointList.add(v006);
		pointList.add(p007);
		pointList.add(b008);
		pointList.add(p009);
		pointList.add(v010);
		pointList.add(p011);
		pointList.add(b012);
		pointList.add(p013);
		pointList.add(p014);
		pointList.add(v015);
		pointList.add(p016);
		pointList.add(p017);
		pointList.add(v018);
		pointList.add(p019);
		pointList.add(v020);
		pointList.add(p021);
		pointList.add(p022);
		pointList.add(v023);
		pointList.add(p024);
		pointList.add(p025);
		pointList.add(v026);
		pointList.add(p027);
		pointList.add(v028);
		pointList.add(p029);
		pointList.add(p030);
		pointList.add(v031);
		pointList.add(p032);
		pointList.add(p033);
		pointList.add(v034);
		pointList.add(p035);
		pointList.add(v036);
		pointList.add(p037);
		pointList.add(p038);
		pointList.add(v039);
		pointList.add(p040);
		pointList.add(p041);
		pointList.add(v042);
		pointList.add(p043);
		pointList.add(b044);
		pointList.add(p045);
		pointList.add(p046);
		pointList.add(p047);
		pointList.add(v048);
		pointList.add(p049);
		pointList.add(p050);
		pointList.add(p051);
		pointList.add(b052);
		pointList.add(p053);
		pointList.add(p054);
		pointList.add(p055);
		pointList.add(v056);
		pointList.add(p057);
		pointList.add(p058);
		pointList.add(p059);
		pointList.add(b060);
		pointList.add(p061);
		pointList.add(v062);
		pointList.add(p063);
		pointList.add(p064);
		pointList.add(v065);
		pointList.add(p066);
		pointList.add(p067);
		pointList.add(v068);
		pointList.add(p069);
		pointList.add(p070);
		pointList.add(v071);
		pointList.add(p072);
		pointList.add(p073);
		pointList.add(b074);
		pointList.add(p075);
		pointList.add(p076);
		pointList.add(v077);
		pointList.add(v078);
		pointList.add(p079);
		pointList.add(p080);
		pointList.add(b081);
		pointList.add(p082);
		pointList.add(p083);
		pointList.add(v084);
		pointList.add(p085);
		pointList.add(p086);
		pointList.add(v087);
		pointList.add(p088);
		pointList.add(p089);
		pointList.add(b090);
		pointList.add(p091);
		pointList.add(p092);
		pointList.add(v093);
		pointList.add(p094);
		pointList.add(v095);
		pointList.add(p096);
		pointList.add(p097);
		pointList.add(v098);
		pointList.add(p099);
		pointList.add(p100);
		pointList.add(v101);
		pointList.add(p102);
		pointList.add(p103);
		pointList.add(v104);
		pointList.add(v105);
		pointList.add(p106);
		pointList.add(p107);
		pointList.add(v108);
		pointList.add(p109);
		pointList.add(p110);
		pointList.add(v111);
		pointList.add(p112);
		pointList.add(p113);
		pointList.add(v114);
		pointList.add(p115);
		pointList.add(v116);
		pointList.add(p117);
		pointList.add(p118);
		pointList.add(b119);
		pointList.add(p120);
		pointList.add(b121);
		pointList.add(p122);
		pointList.add(p123);
		pointList.add(v124);
		pointList.add(p125);
		pointList.add(p126);
		pointList.add(p127);
		pointList.add(b128);
		pointList.add(p129);
		pointList.add(v130);
		pointList.add(p131);
		pointList.add(b132);
		pointList.add(p133);
		pointList.add(p134);
		pointList.add(p135);
		pointList.add(v136);
		pointList.add(p137);
		pointList.add(p138);
		pointList.add(b139);
		pointList.add(p140);
		pointList.add(p141);
		pointList.add(p142);
		pointList.add(p143);
		pointList.add(p144);
		pointList.add(p145);
		pointList.add(p146);
		pointList.add(p147);
		pointList.add(p148);
		pointList.add(p149);
		pointList.add(p150);
		pointList.add(v151);
		
		blockPlaceList.add(b000);
		blockPlaceList.add(b004);
		blockPlaceList.add(b008);
		blockPlaceList.add(b012);
		blockPlaceList.add(b044);
		blockPlaceList.add(b052);
		blockPlaceList.add(b060);
		blockPlaceList.add(b074);
		blockPlaceList.add(b081);
		blockPlaceList.add(b090);
		blockPlaceList.add(b119);
		blockPlaceList.add(b121);
		blockPlaceList.add(b128);
		blockPlaceList.add(b132);
		blockPlaceList.add(b139);
	}

	public static void setBlockPlace(int code){
		int n = code;
		int i = 1;

		blockPlaceIDList[2] = GREEN_PLACE_ID;

		i = (int)(n/K_BLACK);
		n = n - (i*K_BLACK);
		switch(i+1){
			case 1:
				blockPlaceIDList[0] = 0;
				break;
			case 2:
				blockPlaceIDList[0] = 4;
				break;
			case 3:
				blockPlaceIDList[0] = 8;
				break;
			case 4:
				blockPlaceIDList[0] = 12;
				break;
			case 5:
				blockPlaceIDList[0] = 44;
				break;
			case 6:
				blockPlaceIDList[0] = 52;
				break;
			case 7:
				blockPlaceIDList[0] = 60;
				break;
			case 8:
				blockPlaceIDList[0] = 74;
				break;
			case 9:
				blockPlaceIDList[0] = 81;
				break;
			case 10:
				blockPlaceIDList[0] = 90;
				break;
			case 11:
				blockPlaceIDList[0] = 119;
				break;
			case 12:
				blockPlaceIDList[0] = 121;
				break;
			case 13:
				blockPlaceIDList[0] = 128;
				break;
			case 14:
				blockPlaceIDList[0] = 132;
				break;
			case 15:
				blockPlaceIDList[0] = 139;
				break;
		}

		i = (int)(n/K_RED);
		n = n - (i*K_RED);
		switch(i+1){
			case 1:
				blockPlaceIDList[1] = 4;
				break;
			case 2:
				blockPlaceIDList[1] = 8;
				break;
			case 3:
				blockPlaceIDList[1] = 12;
				break;
			case 4:
				blockPlaceIDList[1] = 44;
				break;
			case 5:
				blockPlaceIDList[1] = 52;
				break;
			case 6:
				blockPlaceIDList[1] = 81;
				break;
			case 7:
				blockPlaceIDList[1] = 90;
				break;
			case 8:
				blockPlaceIDList[1] = 119;
				break;
			case 9:
				blockPlaceIDList[1] = 121;
				break;
			case 10:
				blockPlaceIDList[1] = 128;
				break;
			case 11:
				blockPlaceIDList[1] = 139;
				break;
		}

		i = (int)(n/K_YELLOW);
		n = n - (i*K_YELLOW);
		switch(i+1){
			case 1:
				blockPlaceIDList[4] = 0;
				break;
			case 2:
				blockPlaceIDList[4] = 4;
				break;
			case 3:
				blockPlaceIDList[4] = 12;
				break;
			case 4:
				blockPlaceIDList[4] = 52;
				break;
			case 5:
				blockPlaceIDList[4] = 60;
				break;
			case 6:
				blockPlaceIDList[4] = 74;
				break;
			case 7:
				blockPlaceIDList[4] = 81;
				break;
			case 8:
				blockPlaceIDList[4] = 90;
				break;
			case 9:
				blockPlaceIDList[4] = 119;
				break;
			case 10:
				blockPlaceIDList[4] = 121;
				break;
			case 11:
				blockPlaceIDList[4] = 132;
				break;
		}

		i = (int)(n/K_BLUE);
		n = n - (i*K_BLUE);
		switch(i+1){
			case 1:
				blockPlaceIDList[3] = 0;
				break;
			case 2:
				blockPlaceIDList[3] = 8;
				break;
			case 3:
				blockPlaceIDList[3] = 44;
				break;
			case 4:
				blockPlaceIDList[3] = 52;
				break;
			case 5:
				blockPlaceIDList[3] = 60;
				break;
			case 6:
				blockPlaceIDList[3] = 74;
				break;
			case 7:
				blockPlaceIDList[3] = 90;
				break;
			case 8:
				blockPlaceIDList[3] = 119;
				break;
			case 9:
				blockPlaceIDList[3] = 128;
				break;
			case 10:
				blockPlaceIDList[3] = 132;
				break;
			case 11:
				blockPlaceIDList[3] = 139;
				break;
		}
	}

	public static List<Vertex> getConnectionVertex(int pointID){
		List<Vertex> list = new ArrayList<Vertex>();
		if(connectionList.containsKey(pointID)){
			Iterator<Vertex> iterV = connectionList.get(pointID).keySet().iterator();
			while(iterV.hasNext()){
				list.add(iterV.next());
			}
		}
		else{
		}
		return list;
	}

	public static List<Path> getConnectionPath(int pointID){
		List<Path> list = new ArrayList<Path>();
		if(connectionList.containsKey(pointID)){
			Iterator<Path> iterP = connectionList.get(pointID).values().iterator();
			while(iterP.hasNext()){
				list.add(iterP.next());
			}
		}
		else{
		}
		return list;
	}

	public static Path getConnectionPath(int pointID1,int pointID2){
		if((connectionList.containsKey(pointID1) &&(connectionList.get(pointID1).containsKey((Vertex)getPointObject(pointID2))))){
			return connectionList.get(pointID1).get((Vertex)getPointObject(pointID2));
		}
		else{
			return null;
		}
	}

	public static Object getPointObject(int pointID){
		return pointList.get(pointID);
	}

	public static int[] getBlockPlaceIDList() {
		int[] list = (int[])blockPlaceIDList.clone();
		return list;
	}

	public static void setBlockPlaceIDList(int blackID,int redID,int greenID,int blueID,int yellowID) {
		BlockArrangeInfo.blockPlaceIDList[0] = blackID;
		BlockArrangeInfo.blockPlaceIDList[1] = redID;
		BlockArrangeInfo.blockPlaceIDList[2] = greenID;
		BlockArrangeInfo.blockPlaceIDList[3] = blueID;
		BlockArrangeInfo.blockPlaceIDList[4] = yellowID;
	}

	public static Map<Integer, Map<Vertex, Path>> getConnectionList() {
		return connectionList;
	}
	
	public static List<BlockPlace> getBlockPlaceList()
	{
		return blockPlaceList;
	}
	//setConnectionListはバグの元になりそうなので作らない
}
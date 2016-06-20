package it.mate.testleaflet.server.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Country implements Serializable {
  
  private static List<Country> instances = null;
  
  private String name;
  
  private String capital;
  
  private String code;
  
  private String internationalPrefix;
  
  protected Country(String name, String capital, String code, String prefixNumber) {
    super();
    this.name = name;
    this.capital = capital;
    this.code = code;
    this.internationalPrefix = prefixNumber;
  }

  public String getName() {
    return name;
  }

  public String getCapital() {
    return capital;
  }

  public String getCode() {
    return code;
  }

  public String getInternationalPrefix() {
    return internationalPrefix;
  }

  public static List<Country> getList() {
    if (instances != null) {
      return instances;
    }
    instances = new ArrayList<Country>();
    instances.add(new Country("Afghanistan","Kabul","af","93"));
    instances.add(new Country("Albania","Tirana","al","355"));
    instances.add(new Country("Algeria","Algiers","dz","213"));
    instances.add(new Country("AmericanSamoa","PagoPago","as","684"));
    instances.add(new Country("Andorra","Andorra","ad","376"));
    instances.add(new Country("Angola","Luanda","ao","244"));
    instances.add(new Country("Anguilla","TheValley","ai","1-264"));
    instances.add(new Country("Antarctica","None","aq","672"));
    instances.add(new Country("AntiguaandBarbuda","St.Johns","ag","1-268"));
    instances.add(new Country("Argentina","BuenosAires","ar","54"));
    instances.add(new Country("Armenia","Yerevan","am","374"));
    instances.add(new Country("Aruba","Oranjestad","aw","297"));
    instances.add(new Country("Australia","Canberra","au","61"));
    instances.add(new Country("Austria","Vienna","at","43"));
    instances.add(new Country("Azerbaijan","Baku","az","994"));
    instances.add(new Country("Bahamas","Nassau","bs","1-242"));
    instances.add(new Country("Bahrain","Al-Manamah","bh","973"));
    instances.add(new Country("Bangladesh","Dhaka","bd","880"));
    instances.add(new Country("Barbados","Bridgetown","bb","1-246"));
    instances.add(new Country("Belarus","Minsk","by","375"));
    instances.add(new Country("Belgium","Brussels","be","32"));
    instances.add(new Country("Belize","Belmopan","bz","501"));
    instances.add(new Country("Benin","Porto-Novo","bj","229"));
    instances.add(new Country("Bermuda","Hamilton","bm","1-441"));
    instances.add(new Country("Bhutan","Thimphu","bt","975"));
    instances.add(new Country("Bolivia","LaPaz","bo","591"));
    instances.add(new Country("Bosnia-Herzegovina","Sarajevo","ba","387"));
    instances.add(new Country("Botswana","Gaborone","bw","267"));
    instances.add(new Country("BouvetIsland","None","bv",""));
    instances.add(new Country("Brazil","Brasilia","br","55"));
    instances.add(new Country("BritishIndianOceanTerritory","None","io",""));
    instances.add(new Country("BruneiDarussalam","BandarSeriBegawan","bn","673"));
    instances.add(new Country("Bulgaria","Sofia","bg","359"));
    instances.add(new Country("BurkinaFaso","Ouagadougou","bf","226"));
    instances.add(new Country("Burundi","Bujumbura","bi","257"));
    instances.add(new Country("Cambodia","PhnomPenh","kh","855"));
    instances.add(new Country("Cameroon","Yaounde","cm","237"));
    instances.add(new Country("Canada","Ottawa","ca","1"));
    instances.add(new Country("CapeVerde","Praia","cv","238"));
    instances.add(new Country("CaymanIslands","Georgetown","ky","1-345"));
    instances.add(new Country("CentralAfricanRepublic","Bangui","cf","236"));
    instances.add(new Country("Chad","NDjamena","td","235"));
    instances.add(new Country("Chile","Santiago","cl","56"));
    instances.add(new Country("China","Beijing","cn","86"));
    instances.add(new Country("ChristmasIsland","TheSettlement","cx","61"));
    instances.add(new Country("Cocos(Keeling)Islands","WestIsland","cc","61"));
    instances.add(new Country("Colombia","Bogota","co","57"));
    instances.add(new Country("Comoros","Moroni","km","269"));
    instances.add(new Country("Congo","Brazzaville","cg","242"));
    instances.add(new Country("Congo,Dem.Republic","Kinshasa","cd","243"));
    instances.add(new Country("CookIslands","Avarua","ck","682"));
    instances.add(new Country("CostaRica","SanJose","cr","506"));
    instances.add(new Country("Croatia","Zagreb","hr","385"));
    instances.add(new Country("Cuba","Havana","cu","53"));
    instances.add(new Country("Cyprus","Nicosia","cy","357"));
    instances.add(new Country("CzechRep.","Prague","cz","420"));
    instances.add(new Country("Denmark","Copenhagen","dk","45"));
    instances.add(new Country("Djibouti","Djibouti","dj","253"));
    instances.add(new Country("Dominica","Roseau","dm","1-767"));
    instances.add(new Country("DominicanRepublic","SantoDomingo","do","809"));
    instances.add(new Country("Ecuador","Quito","ec","593"));
    instances.add(new Country("Egypt","Cairo","eg","20"));
    instances.add(new Country("ElSalvador","SanSalvador","sv","503"));
    instances.add(new Country("EquatorialGuinea","Malabo","gq","240"));
    instances.add(new Country("Eritrea","Asmara","er","291"));
    instances.add(new Country("Estonia","Tallinn","ee","372"));
    instances.add(new Country("Ethiopia","AddisAbaba","et","251"));
    instances.add(new Country("EuropeanUnion","Brussels","eu.int",""));
    instances.add(new Country("FalklandIslands(Malvinas)","Stanley","fk","500"));
    instances.add(new Country("FaroeIslands","Torshavn","fo","298"));
    instances.add(new Country("Fiji","Suva","fj","679"));
    instances.add(new Country("Finland","Helsinki","fi","358"));
    instances.add(new Country("France","Paris","fr","33"));
    instances.add(new Country("FrenchGuiana","Cayenne","gf","594"));
    instances.add(new Country("FrenchSouthernTerritories","None","tf",""));
    instances.add(new Country("Gabon","Libreville","ga","241"));
    instances.add(new Country("Gambia","Banjul","gm","220"));
    instances.add(new Country("Georgia","Tbilisi","ge","995"));
    instances.add(new Country("Germany","Berlin","de","49"));
    instances.add(new Country("Ghana","Accra","gh","233"));
    instances.add(new Country("Gibraltar","Gibraltar","gi","350"));
    instances.add(new Country("GreatBritain","London","gb","44"));
    instances.add(new Country("Greece","Athens","gr","30"));
    instances.add(new Country("Greenland","Godthab","gl","299"));
    instances.add(new Country("Grenada","St.Georges","gd","1-473"));
    instances.add(new Country("Guadeloupe(French)","Basse-Terre","gp","590"));
    instances.add(new Country("Guam(USA)","Agana","gu","1-671"));
    instances.add(new Country("Guatemala","GuatemalaCity","gt","502"));
    instances.add(new Country("Guernsey","St.PeterPort","gg",""));
    instances.add(new Country("Guinea","Conakry","gn","224"));
    instances.add(new Country("GuineaBissau","Bissau","gw","245"));
    instances.add(new Country("Guyana","Georgetown","gy","592"));
    instances.add(new Country("Haiti","Port-au-Prince","ht","509"));
    instances.add(new Country("HeardIslandandMcDonaldIslands","None","hm",""));
    instances.add(new Country("Honduras","Tegucigalpa","hn","504"));
    instances.add(new Country("HongKong","Victoria","hk","852"));
    instances.add(new Country("Hungary","Budapest","hu","36"));
    instances.add(new Country("Iceland","Reykjavik","is","354"));
    instances.add(new Country("India","NewDelhi","in","91"));
    instances.add(new Country("Indonesia","Jakarta","id","62"));
    instances.add(new Country("Iran","Tehran","ir","98"));
    instances.add(new Country("Iraq","Baghdad","iq","964"));
    instances.add(new Country("Ireland","Dublin","ie","353"));
    instances.add(new Country("IsleofMan","Douglas","im",""));
    instances.add(new Country("Israel","Jerusalem","il","972"));
    instances.add(new Country("Italy","Rome","it","39"));
    instances.add(new Country("IvoryCoast","Abidjan","ci","225"));
    instances.add(new Country("Jamaica","Kingston","jm","1-876"));
    instances.add(new Country("Japan","Tokyo","jp","81"));
    instances.add(new Country("Jersey","SaintHelier","je",""));
    instances.add(new Country("Jordan","Amman","jo","962"));
    instances.add(new Country("Kazakhstan","Astana","kz","7"));
    instances.add(new Country("Kenya","Nairobi","ke","254"));
    instances.add(new Country("Kiribati","Tarawa","ki","686"));
    instances.add(new Country("Korea-North","Pyongyang","kp","850"));
    instances.add(new Country("Korea-South","Seoul","kr","82"));
    instances.add(new Country("Kuwait","KuwaitCity","kw","965"));
    instances.add(new Country("Kyrgyzstan","Bishkek","kg","996"));
    instances.add(new Country("Laos","Vientiane","la","856"));
    instances.add(new Country("Latvia","Riga","lv","371"));
    instances.add(new Country("Lebanon","Beirut","lb","961"));
    instances.add(new Country("Lesotho","Maseru","ls","266"));
    instances.add(new Country("Liberia","Monrovia","lr","231"));
    instances.add(new Country("Libya","Tripoli","ly","218"));
    instances.add(new Country("Liechtenstein","Vaduz","li","423"));
    instances.add(new Country("Lithuania","Vilnius","lt","370"));
    instances.add(new Country("Luxembourg","Luxembourg","lu","352"));
    instances.add(new Country("Macau","Macau","mo","853"));
    instances.add(new Country("Macedonia","Skopje","mk","389"));
    instances.add(new Country("Madagascar","Antananarivo","mg","261"));
    instances.add(new Country("Malawi","Lilongwe","mw","265"));
    instances.add(new Country("Malaysia","KualaLumpur","my","60"));
    instances.add(new Country("Maldives","Male","mv","960"));
    instances.add(new Country("Mali","Bamako","ml","223"));
    instances.add(new Country("Malta","Valletta","mt","356"));
    instances.add(new Country("MarshallIslands","Majuro","mh","692"));
    instances.add(new Country("Martinique(French)","Fort-de-France","mq","596"));
    instances.add(new Country("Mauritania","Nouakchott","mr","222"));
    instances.add(new Country("Mauritius","PortLouis","mu","230"));
    instances.add(new Country("Mayotte","Dzaoudzi","yt","269"));
    instances.add(new Country("Mexico","MexicoCity","mx","52"));
    instances.add(new Country("Micronesia","Palikir","fm","691"));
    instances.add(new Country("Moldova","Kishinev","md","373"));
    instances.add(new Country("Monaco","Monaco","mc","377"));
    instances.add(new Country("Mongolia","UlanBator","mn","976"));
    instances.add(new Country("Montenegro","Podgorica","me","382"));
    instances.add(new Country("Montserrat","Plymouth","ms","1-664"));
    instances.add(new Country("Morocco","Rabat","ma","212"));
    instances.add(new Country("Mozambique","Maputo","mz","258"));
    instances.add(new Country("Myanmar","Naypyidaw","mm","95"));
    instances.add(new Country("Namibia","Windhoek","na","264"));
    instances.add(new Country("Nauru","Yaren","nr","674"));
    instances.add(new Country("Nepal","Kathmandu","np","977"));
    instances.add(new Country("Netherlands","Amsterdam","nl","31"));
    instances.add(new Country("NetherlandsAntilles","Willemstad","an","599"));
    instances.add(new Country("NewCaledonia(French)","Noumea","nc","687"));
    instances.add(new Country("NewZealand","Wellington","nz","64"));
    instances.add(new Country("Nicaragua","Managua","ni","505"));
    instances.add(new Country("Niger","Niamey","ne","227"));
    instances.add(new Country("Nigeria","Lagos","ng","234"));
    instances.add(new Country("Niue","Alofi","nu","683"));
    instances.add(new Country("NorfolkIsland","Kingston","nf","672"));
    instances.add(new Country("NorthernMarianaIslands","Saipan","mp","670"));
    instances.add(new Country("Norway","Oslo","no","47"));
    instances.add(new Country("Oman","Muscat","om","968"));
    instances.add(new Country("Pakistan","Islamabad","pk","92"));
    instances.add(new Country("Palau","Koror","pw","680"));
    instances.add(new Country("Panama","PanamaCity","pa","507"));
    instances.add(new Country("PapuaNewGuinea","PortMoresby","pg","675"));
    instances.add(new Country("Paraguay","Asuncion","py","595"));
    instances.add(new Country("Peru","Lima","pe","51"));
    instances.add(new Country("Philippines","Manila","ph","63"));
    instances.add(new Country("PitcairnIsland","Adamstown","pn",""));
    instances.add(new Country("Poland","Warsaw","pl","48"));
    instances.add(new Country("Polynesia(French)","Papeete","pf","689"));
    instances.add(new Country("Portugal","Lisbon","pt","351"));
    instances.add(new Country("PuertoRico","SanJuan","pr","1-787"));
    instances.add(new Country("Qatar","Doha","qa","974"));
    instances.add(new Country("Reunion(French)","Saint-Denis","re","262"));
    instances.add(new Country("Romania","Bucharest","ro","40"));
    instances.add(new Country("Russia","Moscow","ru","7"));
    instances.add(new Country("Rwanda","Kigali","rw","250"));
    instances.add(new Country("SaintHelena","Jamestown","sh","290"));
    instances.add(new Country("SaintKitts&NevisAnguilla","Basseterre","kn","1-869"));
    instances.add(new Country("SaintLucia","Castries","lc","1-758"));
    instances.add(new Country("SaintPierreandMiquelon","St.Pierre","pm","508"));
    instances.add(new Country("SaintVincent&Grenadines","Kingstown","vc","1-784"));
    instances.add(new Country("Samoa","Apia","ws","684"));
    instances.add(new Country("SanMarino","SanMarino","sm","378"));
    instances.add(new Country("SaoTomeandPrincipe","SaoTome","st","239"));
    instances.add(new Country("SaudiArabia","Riyadh","sa","966"));
    instances.add(new Country("Senegal","Dakar","sn","221"));
    instances.add(new Country("Serbia","Belgrade","rs","381"));
    instances.add(new Country("Seychelles","Victoria","sc","248"));
    instances.add(new Country("SierraLeone","Freetown","sl","232"));
    instances.add(new Country("Singapore","Singapore","sg","65"));
    instances.add(new Country("Slovakia","Bratislava","sk","421"));
    instances.add(new Country("Slovenia","Ljubljana","si","386"));
    instances.add(new Country("SolomonIslands","Honiara","sb","677"));
    instances.add(new Country("Somalia","Mogadishu","so","252"));
    instances.add(new Country("SouthAfrica","Pretoria","za","27"));
    instances.add(new Country("SouthGeorgia&SouthSandwichIslands","None","gs",""));
    instances.add(new Country("SouthSudan","Ramciel","ss",""));
    instances.add(new Country("Spain","Madrid","es","34"));
    instances.add(new Country("SriLanka","Colombo","lk","94"));
    instances.add(new Country("Sudan","Khartoum","sd","249"));
    instances.add(new Country("Suriname","Paramaribo","sr","597"));
    instances.add(new Country("SvalbardandJanMayenIslands","Longyearbyen","sj",""));
    instances.add(new Country("Swaziland","Mbabane","sz","268"));
    instances.add(new Country("Sweden","Stockholm","se","46"));
    instances.add(new Country("Switzerland","Bern","ch","41"));
    instances.add(new Country("Syria","Damascus","sy","963"));
    instances.add(new Country("Taiwan","Taipei","tw","886"));
    instances.add(new Country("Tajikistan","Dushanbe","tj","992"));
    instances.add(new Country("Tanzania","Dodoma","tz","255"));
    instances.add(new Country("Thailand","Bangkok","th","66"));
    instances.add(new Country("Togo","Lome","tg","228"));
    instances.add(new Country("Tokelau","None","tk","690"));
    instances.add(new Country("Tonga","Nukualofa","to","676"));
    instances.add(new Country("TrinidadandTobago","PortofSpain","tt","1-868"));
    instances.add(new Country("Tunisia","Tunis","tn","216"));
    instances.add(new Country("Turkey","Ankara","tr","90"));
    instances.add(new Country("Turkmenistan","Ashgabat","tm","993"));
    instances.add(new Country("TurksandCaicosIslands","GrandTurk","tc","1-649"));
    instances.add(new Country("Tuvalu","Funafuti","tv","688"));
    instances.add(new Country("U.K.","London","uk","44"));
    instances.add(new Country("Uganda","Kampala","ug","256"));
    instances.add(new Country("Ukraine","Kiev","ua","380"));
    instances.add(new Country("UnitedArabEmirates","AbuDhabi","ae","971"));
    instances.add(new Country("Uruguay","Montevideo","uy","598"));
    instances.add(new Country("USA","Washington","us","1"));
    instances.add(new Country("USAMinorOutlyingIslands","None","um",""));
    instances.add(new Country("Uzbekistan","Tashkent","uz","998"));
    instances.add(new Country("Vanuatu","PortVila","vu","678"));
    instances.add(new Country("Vatican","VaticanCity","va","39"));
    instances.add(new Country("Venezuela","Caracas","ve","58"));
    instances.add(new Country("Vietnam","Hanoi","vn","84"));
    instances.add(new Country("VirginIslands(British)","RoadTown","vg","1-284"));
    instances.add(new Country("VirginIslands(USA)","CharlotteAmalie","vi","1-340"));
    instances.add(new Country("WallisandFutunaIslands","Mata-Utu","wf","681"));
    instances.add(new Country("WesternSahara","ElAaiun","eh",""));
    instances.add(new Country("Yemen","Sana","ye","967"));
    instances.add(new Country("Zambia","Lusaka","zm","260"));
    instances.add(new Country("Zimbabwe","Harare","zw","263"));
    return instances;
  }

}

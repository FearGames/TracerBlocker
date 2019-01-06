
package cz.GravelCZLP.TracerBlocker;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * Copyright 2016 Luuk Jacobs

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class RandomNameGenerator {
	private static final List<String> names = Arrays.asList("Famousinex", "FantasticBroadcast", "FanTrue", "FarChilled",
			"FashionDigest", "Featureelse", "FeistyDoom", "FleaSkunky", "Footballat", "Fortunewp", "Fourbolthe",
			"Foxtonium", "Foxtosenf", "Freedaco", "FriedInterior", "FunLinkin", "Gatownplex", "GazetteBrood",
			"Geoniati", "GetLord", "GigaRoyalThe", "Gigglyshta", "GinoQuant", "Ginorabuni", "Glensumen", "Gloastar",
			"Gnomelacess", "GnomeTrumpComic", "Godzillali", "Goldiverl", "GoodXglossy", "GoofyVirtuoso", "Gooseller",
			"Gotrasp", "Graycyc", "Greatelista", "Greatlate", "Greelsonda", "GreyDarkKenji", "GurlRida", "Gyrocus",
			"HappyViking", "Headephome", "Heavino", "HempPeatear", "HeraldEpicGlimmer", "HinchUpdate", "HipPower",
			"Hipurniview", "Histocome", "HockeyHappy", "Hottonixli", "IceVengeance", "IdealDonald", "Idolerwave",
			"Immerisha", "Ingenade", "Insuperf", "Interestli", "Intuitalo", "Ironkso", "Isfarbolo", "IslandThere",
			"Iwantaxin", "Jadensosonl", "JameDiddyPierce", "JameFreexGuanto", "Jeansombane", "JimBlog", "Joetess",
			"Journalle", "JuzBlackenShiya", "KeeperPeatear", "Kentrostrea", "Khadionitz", "KiddoTown", "Kingovalth",
			"Kirigesh", "Koolveland", "Kurisuccan", "Ladeanag", "LadyFlirty", "Landesfiel", "Landorigau", "Laughamigit",
			"Lawindatase", "Laxradiusp", "LeadCasual", "Leongoughl", "Lessatchie", "LessSpunky", "Letteriumg",
			"Lillyauto", "Linedincelp", "LoveBlonde", "Lucyberket", "LunaticChampion", "MaddGorgeous", "MaddProWater",
			"Mainber", "MasterTeen", "Maximothe", "Mediabern", "Medsata", "Megalth", "Messagesia", "MewTwoShopping",
			"Millierer", "Minderland", "MinyLovely", "Miraclekath", "Mittleal", "Abercont", "Aboversen", "Acinerso",
			"Activer", "Advermack", "AimIan", "AirMrDiddy", "AlliZena", "AngelsTins", "Anharthsco", "Animatichar",
			"Answerenv", "ApenguinReally", "Apnator", "Arlandus", "Atorstre", "Avelisto", "BagoList", "Bandbox",
			"Baronixtu", "Basicos", "Basionle", "BauerAngel", "Bearednaar", "BeastChoneHeadline", "BeatRock",
			"Belcharssel", "BingFantastic", "Birdaxan", "BirdTagz", "BitSlay", "Blabassour", "BlackenLife", "Blinkedia",
			"BloggerEat", "BlondMajor", "BoardinCent", "Bobsgenicta", "BobYoung", "Bondarsco", "Botardivi",
			"BoxCoverage", "Boxwooday", "Breezendsa", "Brideamp", "Buffleleto", "Cajarver", "Campycost", "Candynabu",
			"Cartdeusol", "CartSpot", "Celplicie", "ChanCincoSweetie", "Chanielsan", "Cheeruption", "ChikPatty",
			"Chiquitalp", "Chmandel", "ChosenTaruDev", "Chrisme", "ChromePostKnight", "Chrometal", "Cideance",
			"ClearToxic", "Cleartscast", "ClownSoccer", "ComicPac", "CommentsChampion", "Compulat", "Confostep",
			"Conimene", "Contenticks", "Coolahersk", "CoopsWow", "Cosizeba", "Covinerco", "Creamplic", "CrownFlash",
			"Crumpre", "CrunchLetter", "Crunchoris", "Cuddlyonck", "Czarriumbn", "DailiesEdgy", "Dailyport",
			"DarthTricky", "Dataphe", "DeanQuant", "Deckettion", "Deckinessi", "Dextruny", "DigestPhreek", "Discoverco",
			"Dispenti", "Doomakerge", "Dravendist", "Draytexco", "Drughpitie", "Drummerto", "DubyaCookie", "Duckuphi",
			"EarZolloShark", "Eatective", "EdgyWise", "Editorlder", "Editprosco", "Egtonhe", "Emagium", "Emargomy",
			"Euclame", "ExecHehe", "ExtraXmc", "FaceLegend", "Mixiuser", "Mohawkning", "Monsterling", "MonWill",
			"Motoppoche", "Mottefern", "Mscopersca", "Mucharisse", "Murphydrei", "Nardbooz", "Ncirchree", "Nessberges",
			"NessLunatic", "Netfield", "Nethest", "Nicelleyco", "Nicerviserr", "Ninjargenom", "Ookeolym", "Oundanst",
			"Pairvirg", "Parlastem", "Peachesysbu", "Penguinness", "PenguinXoxo", "Peptoin", "Percented", "Persoft",
			"Phobiclenn", "PhreekKuroKhad", "PlaceLetter", "Playeronne", "Plentyroni", "Plurenti", "Prepeersto",
			"PricelessIntincr", "Pridgett", "Prioreh", "Punkyapper", "Pyxismou", "Quickerli", "Racterli", "Rainboweron",
			"Rainbownsen", "RapBest", "Readynabe", "Redmorest", "Reesecureav", "Reporterie", "ReporterRely", "Rereened",
			"Rickshet", "RiderCurious", "RingThink", "Rollinkler", "Rollowmobi", "Rosenteeso", "Runningfibr", "Ruthead",
			"Sakillirki", "Sarenhawk", "Scaryokersh", "Sconnada", "Seranen", "Shadowrynx", "Shatorient", "ShcamVivala",
			"Sk8rturraz", "Skateriair", "Sliminessi", "Smomylyl", "Softhamser", "Somentwave", "Specine", "StaceyPeach",
			"", "FallenPunk", "FallsChee", "FeaturedSporks", "FelineDonald", "FighterUout", "Firstoculat", "Fixyambus",
			"Fizzeterra", "Flamesashn", "FlavoredHippo", "FlavoredKuro", "Flecialp", "Flintabey", "Flubdan", "Foldogra",
			"FourNewscast", "Freezingan", "Frilled", "Funmontan", "Funnymyxolo", "Fusionven", "FuzzyXanRainbow",
			"Galasxml", "GalFan", "GameRosaTheborg", "Gasserwa", "GazerGloryGenius", "Gazettersat", "GhoulReptile",
			"Glitzenpa", "Globeesta", "GoobleWillShin", "Gotterse", "Grameyst", "Gransett", "Greeneripry", "Grimeterch",
			"Grionson", "Grundynovi", "GrundyTaru", "GuantoRight", "Guidenvi", "Habassemd", "Hamlendag", "Handsaven",
			"Handynage", "HannahSimple", "Haushist", "HeadRider", "HeavenJiggy", "HelpKino", "Helsero", "Hippomafl",
			"Hoarhamai", "HondaXglossy", "Honearra", "Horseas", "Iambarage", "Icebogyny", "IcyBeatWave",
			"IcyRainbowNeat", "Idealtity", "Infonicur", "Ingeouth", "InsiderApenguin", "InsideSra", "Insightques",
			"Insumet", "Interested", "Interiorah", "Inventlysi", "Itadvism", "IteXboxChunky", "Izintele", "Jamentapol",
			"Jeanzainte", "JewelPhat", "Jingoscaliz", "Joreyef", "Kaptainuori", "Karlands", "Keepuphout", "Kidzwit",
			"Kinovatick", "Kixggerstru", "Kluguardba", "Kontrum", "KroolSnoop", "Lawscantl", "LaxrDoom", "Leandyi",
			"Ledgerertr", "LeventisAnnon", "LeventisSister", "LightIan", "Likessedin", "Limeetitce", "Linkinimin",
			"Linkintena", "Loboured", "LogicRavager", "Lopsemple", "Lucygnitgu", "LuvLinkin", "LyfeKentros",
			"Mabellbio", "Maidantlog", "MajereDressy", "Majerestru", "Majoratosca", "MamaAquatic", "Mamationpo",
			"Mandinger", "Mansathemi", "Marcsiden", "MarkJava", "MasterAngel", "Matitype", "McBase", "Meristba",
			"Mertzelia", "MinyGame", "Mistereter", "2tochuBurke", "2tochuChirp", "A1MusicPleasant", "Acetmang",
			"Advanel", "Airbursky", "AloneRappaRpg", "Americast", "Amoleia", "AnarchyJuzMama", "Anecdoc",
			"AnnouncerFeature", "Arbitalec", "Ardelise", "Armorta", "Attringdo", "Auedlimo", "AuthorBreaking",
			"Awayersorr", "Babixzaust", "Baringer", "Belmaride", "Beverne", "Bindermyli", "Bizarrenarg", "BlondBubble",
			"BlondHearStripper", "BlondKidDonald", "Bluesto", "Bobooksolu", "BoltThereUnlimited", "BorgMind",
			"BoxWowJosh", "Brainte", "Bridmidd", "BrightFlavored", "Budcobs", "BugsSmugPeatear", "Burntusbrid",
			"BuzzStoop", "Byterfetti", "ByteSk8r", "Cabinte", "Caporcide", "CaptainSablink", "Captainsubl", "Cardstr",
			"CasualFlirty", "Casualkonz", "Certblocat", "Cheelcanti", "ChellSoftFuzzy", "Chemiapi", "ChiquitaPool",
			"Chirpanti", "ChoneVital", "Cindity", "ComfyVivala", "ContentFantasy", "Controgik", "CookieMissBoost",
			"Cookievane", "CornyThedevil", "Cortmobi", "Cottonixml", "CoverIon", "CrawlerWs", "CrayonKnight",
			"CraziiSmooth", "Crescentum", "Cucosfer", "Datalka", "Daytaint", "Dealastat", "Dealefula", "DelightSpyder",
			"Deluxement", "Demonhemm", "Dermesse", "Desionba", "Devizing", "Dewcleris", "Diagonalit", "Dreamascard",
			"DubyaSaiyan", "Dudeciblue", "Dutkolb", "Eatsyountel", "Ecesion", "Enroparo", "Entaskyh", "Eternalls",
			"Etreveti", "Eutocke", "ExclusiveGuanto", "Expercoto", "Mollung", "Monkeynics", "Muezzle", "MusicIam",
			"Mystingwo", "Myworterud", "Nearlyseac", "Newsphe", "Nitrati", "Nonvizing", "Norditi", "Nozygoldho",
			"NozyWunder", "Nucoatra", "Numetri", "Olicontr", "Omnivater", "Onrequide", "PacXbox", "Panderi",
			"Paneticks", "Paydidia", "PeachJame", "Percept", "Peridgene", "PersonalPride", "Pilolome", "Pinfarmax",
			"Pionesila", "PiraNicerLuv", "Pledguard", "Plentyform", "Ploteronwi", "Plumptab", "Plustrixah",
			"PreciseInspiring", "Presserat", "PrestigeBird", "Primnerry", "Printrald", "Procoqua", "Promonma",
			"Prudyne", "Purpleratu", "Pyosionia", "Quickerer", "QuoteTear", "Raboffi", "RaeGiveThere", "Ravagerpoin",
			"Ravenence", "RavenPopular", "Razulthera", "ReallyGrimPuff", "Reallytion", "ReptileTough", "Rescanta",
			"Rodeoperige", "Roseshlix", "RunGrabsPeach", "SaiyanNephew", "SakiXxTary", "Sandaequie", "Saventer",
			"Sedistop", "Sertheal", "Shadeschfu", "Sharaina", "Shuniveway", "SimonThug", "SkaterAware", "SlayrExpert",
			"SlyHehe", "Smarance", "SnoIanDemon", "Solconic", "SolidStud", "Solomonchbu", "Spyderamex", "Steverspa",
			"TalentedMadd", "", "Fallstaco", "FantasyHead", "FarGutsy", "FarMegs", "FateAholic", "Fecunis", "Ferbelva",
			"Feudanian", "Finalbilect", "FinalFreakOlympic", "FinalWunder", "Finsoft", "Fixtuitsoft", "Flaironho",
			"FluentChronos", "Foreveram", "Fortunerath", "FourSkaterSk8r", "Foxinctatar", "Frasertype", "FreeSkillful",
			"FreshHead", "FriedStone", "FrogScan", "FullyRonz", "FunkyPoet", "Furzett", "GalZest", "GamerDressy",
			"Garnettence", "Gazerplogen", "GigaWriting", "Ginnyonix", "GoGravityFamous", "GoodLucGiga", "Gottabakron",
			"GrandYounger", "Griffonymis", "Grimaferve", "GrimLudaMessage", "Hearkne", "HearSports", "Hemitherd",
			"Hemposmule", "HempPanetDubya", "HeroMuch", "Herosaygam", "Hikaldi", "HiKnight", "Hollusia", "Hopsale",
			"Hotonosi", "IamaBabixz", "Icomerio", "Imatrock", "Indocur", "Inflarket", "Injuster", "Inkgrota",
			"Inlovertax", "InspiringAnnouncer", "Interame", "Interpany", "Intincrippa", "InvaderTwilight", "Invener",
			"InventCorny", "Ionideata", "Isogran", "Itchwork", "Izeeharmat", "Jackpot", "Janserous", "Jerecons",
			"JiggyTroikRoach", "Jinnyanya", "Joanadera", "Jobsoltelo", "JoshJameShow", "Jottokompa", "Katronixty",
			"Keepupdati", "Kenkalonerb", "Khadurcean", "Kirlocker", "KittyPhia", "Kiwinegamb", "KlugForum",
			"Kurisupeop", "Lacquix", "LandList", "Landorteus", "Laughtjetic", "LawSly", "LawZero", "Lessalance",
			"LeventisInformation", "LikeDigestKing", "Litabile", "Localtronin", "LocalWomanNews", "Logicatedge",
			"Logypsie", "LordThere", "Ludanwi", "LyfeSchool", "Lyfetasqua", "Mafiauxys", "MajorSanta", "MamaLand",
			"Maricarp", "MasterBago", "MatrixSter", "Mattertaff", "McIcyLucy", "MegsManiak", "Melikevi", "Melytick",
			"MercySlip", "MissingBest", "Mitzinali", "2hotCrown", "Activectec", "Adrinasa", "Afferick", "Agionspo",
			"Agnosed", "Aiderbo", "Aidhundh", "AirSunBubble", "AnimeHi", "Annexperi", "Antersui", "Appoleter",
			"Apptinga", "AprilBird", "AraLive", "Aramwood", "Archasy", "Ariewing", "ArticleChapter", "ArticlesHippo",
			"BabixzZippo", "Bachipso", "Badmiffee", "BagoTarget", "BalNetworkSolomon", "Beckman", "Belddigian",
			"BelSnoopy", "Bernapt", "Beservil", "Bestomacol", "Biondget", "Bitekerista", "Blazentus", "BlinkReady",
			"Blondielder", "BlondieWasabi", "BoardKaven", "Bocambal", "BotWater", "BoxBlabFace", "Boz2cute", "Brabuste",
			"BradleyMagazine", "BrainySolomon", "Breputhc", "BristleStrong", "Bubbleryza", "Bunnymyruva", "Burkale",
			"Burkingta", "Buypane", "BWithantion", "CakeSuper", "Carbordam", "Celebla", "Cessibas", "Champulet",
			"ChapterWise", "Chatfinect", "Chirootistm", "Chosenpack", "Chronosity", "CincoAnn", "Classainp",
			"Classymbar", "Comburyst", "Comickshipt", "ConnWubbaFly", "Contentur", "Contical", "Cookyphil", "Copaitone",
			"Corioneta", "CoverWow", "CrescentGlimmer", "Crobaler", "Daletang", "DanceBreeze", "Dancedoun",
			"Darkvenali", "DasBorg", "Defenet", "Delightfi", "DeluxePrep", "Dicastru", "Dicatire", "Dipityport",
			"Disoureu", "Dnestica", "Doblemarr", "Dollhedwoo", "Donaldegui", "Doriance", "Dossrose", "Dragonalst",
			"DrBeachGrabs", "Dreamyceti", "Ellyancy", "Endogix", "Erciysk", "ExclusiveSteen", "Modusroa", "MoffWeaver",
			"Moritell", "Mororya", "Mousear", "Mudsuraff", "Mundring", "Myallower", "Myosoftvu", "Nadeckac",
			"NanIzJung", "Natecoolep", "NathHorrayEats", "Naverdex", "Nayboriate", "Nayborkell", "NearlyViking",
			"Nedercape", "Nedrans", "Netstbala", "Nicelywebr", "Nitecher", "Nitymann", "NoticeIz", "Novaxer", "Odoteki",
			"Opercapdr", "Pambitney", "Pandorreb", "PassionWakeboard", "PeachThehibiki", "Peccarsay", "Pellerve",
			"Perchsto", "Perisha", "Pethilabs", "Pinchoint", "PlayAmy", "Plecabub", "Plusillona", "Ponginouse",
			"PonyKnot", "PoolThedevil", "PopularSimply", "PowerWaves", "Progege", "Proggener", "PurpleSaren",
			"QuantSand", "Quantyxinte", "Quinitle", "Racalcat", "RainbowFlashy", "Raphandwe", "RatSheer", "Readiasite",
			"RealEnergySing", "Reggaeroli", "Reggaeumby", "Rejource", "ReportHart", "ReportMagic", "Ringspecon",
			"RipBunny", "RocksJournal", "Rodeowateli", "Saludboat", "Samberop", "Shabbyzami", "Shadowburg", "Shincerr",
			"Shoespowe", "Sionixod", "Smughter", "Stormedia", "Stratecti", "", "FallenUn", "Famendyb", "Fashional",
			"Feistypene", "Ficevisi", "Ficollen", "Figeantl", "FlyTigerYugi", "ForbThega", "Forcellet", "Fragrob",
			"Freakeoff", "Fundase", "Funkylynd", "Funnymono", "FuzzyHinch", "Garrelev", "Genmark", "Genontsw",
			"Getaway", "Gingerve", "Gliostra", "Godwiedelin", "GoldenZeroLight", "Golderylor", "Gotcanelnet",
			"GrandVirtuoso", "Greater", "GreenChrome", "Greenham", "Hannahnlab", "Headpland", "Hellotoma", "Hellowfor",
			"Helpfullcra", "Helsali", "HenryFear", "Heronicaro", "Hiroclosto", "Hoempink", "Hogarest", "Hondatabian",
			"HunterPaper", "Huntertsen", "IfallRock", "Indicom", "InfernoShiny", "Inkbraco", "InsiderKhad",
			"InstantPhat", "InterviewPeatear", "Intincronal", "Inventonsu", "Iourcell", "Isotobarc", "IzDreamy",
			"Jabelvil", "JameTruck", "Janettendin", "Jeanthexce", "JideCottonBaby", "Jollysinima", "Josherzoca",
			"KaiBristleHippo", "Karicard", "Karmali", "KatTin", "KenkaArticle", "Kilgatent", "KinoVampire", "Kissoral",
			"KnotInformer", "Kochryds", "Koolutesta", "Kredian", "KroolEver", "Lageomni", "Lastilet", "Laughtingge",
			"Laundin", "Leenabeaz", "Linefing", "LiveSubject", "Lobschele", "Loeshpoi", "LogA1Cyber", "LogAwareGlamour",
			"Logwarebe", "LordBWithRocket", "LordManiak", "LucyBreaking", "LummoApenguin", "Majereinco", "ManiakPink",
			"Markupways", "Materca", "MdoggEnjoyEpic", "Meddisu", "Mellowwall", "MelShades", "Meltzin", "Mentank",
			"Minetcomax", "Minguest", "MissingBomber", "2freeChrome", "2hotDouble", "AboutPower", "Acetailcola",
			"Acionica", "ActiveGotta", "Adgamyja", "Aidankswa", "Alecogn", "AlertDictator", "Alextrad", "Alryboek",
			"Analyre", "AnhartTricky", "AnimeStunna", "AniTacticXx", "Apsisco", "Aquatickybo", "Atioinfr", "AwareRaven",
			"Bankoller", "Barkency", "Basemedle", "BaseNeo", "BePleasant", "Berlightli", "Biggarkids", "BillionLead",
			"Bipedic", "Bluetitine", "Boardzone", "Boltragret", "Boltysmica", "Booshrobico", "Bootinessi", "Borgithagm",
			"BradleyGhoul", "Bristlean", "BristleFreak", "BroadcastWarm", "Broadim", "BugsCuddly", "Bulliche",
			"Bunracarp", "Burntastway", "Canionne", "Carburyan", "Cascrite", "Caterisys", "Celebason", "Centaxeri",
			"Centemp", "Centhist", "CeticThehibiki", "ChampionSpoiled", "Chantor", "CharmsGarnett", "CharmsNipClassy",
			"Chaterload", "Cheeseconi", "Chestori", "ChiriZap", "Chorksbu", "ChosenBlacken", "Chronosysha", "Cidegold",
			"Cistatcant", "Clavexcel", "Climektr", "Compulte", "Connetta", "CooledEssence", "Coolightma", "Corpodr",
			"Cosmigger", "Crashapatta", "Crawlersm", "CrispSellAmy", "Cutechouse", "Dataexad", "Deanness", "DeepUout",
			"Deplexim", "Dirtyress", "DirtyThin", "DiscoverTagz", "Diverforg", "Dogglepaci", "Doomakerge",
			"Dotercologi", "Druttelly", "Dumberce", "Eatsyoute", "Ecosonixx", "Eforesoft", "Elenide", "EliteYes",
			"Embedia", "Encedias", "EnergyProGold", "Epconauto", "Epicablerb", "Equimedly", "ExecLuda", "Execoughtu",
			"Exemedim", "Eyesiathre", "Mogadst", "Monsiote", "MonUn", "Moortsma", "Motomanico", "Muktaline", "Muresayi",
			"MyAloneLunatic", "Mysonetr", "Nadekana", "Nansmont", "Nelvage", "Neonatingan", "NetworkBlack", "Ninklemi",
			"NipConspiracy", "Nitestel", "Notabelinit", "NotesTwoLime", "Number1etai", "Nyctatinged", "Opmentyl",
			"Orbarqit", "Ouraysu", "Ovectota", "Pandurr", "Panetedboxl", "Papackpifi", "Pariscopy", "Paroblou",
			"Patieldverd", "Pattyping", "Pectorte", "Peopley", "Phoenixer", "PhreekEditor", "PinchTrump", "PinkBorn2",
			"Piranogr", "Placedual", "PlanetYugi", "Plastrail", "Ponyoniary", "Pooleyecto", "PoolKenji", "Popularca",
			"PopularKhad", "PrimeAlpha", "Proolsca", "Proudention", "Pulsoncy", "PuppyMessages", "PurfectFreex",
			"Pyrettext", "Qualreal", "Quipsist", "RavagerTools", "ReportCute", "Rerealle", "RightGato",
			"RiseBagoTwilight", "RockInfamous", "RocksDreamy", "Rodeoperige", "Rosaksconn", "Rosenrechs",
			"RoseQuoteGotta", "Roxieldlyba", "RozFix", "Rucheneph", "Runningos", "Ruthecksi", "Ryothantobr", "Scanixon",
			"ScanWil", "SeenWell", "Selfchorsh", "Shangeredib", "ShardIssue", "ShiyaLadyCrossed", "Skypicompli",
			"SmugOlympic", "Snooptive", "SomberGazer", "Sonicomon", "SosaCartBoosh", "SpoonAgentDeep", "StrongCrescent",
			"", "Fathamix", "Featuream", "Feistymber", "FeistySport", "FelineVashInformation", "FestiveFlea", "Fhiosto",
			"FinestEarTheborg", "Firmyael", "Firstarla", "FirstWorman", "FixSkate", "Flashypnet", "Flirtyonti",
			"Fluidgear", "Flytelenart", "Fopricea", "Forminum", "Forumgeiq", "Fredittech", "Frusticat", "Fullysignif",
			"Fundataraph", "Fundeler", "Gambitecag", "Gamepro", "GamerKenjiV2", "GeneralBear", "Geniuspitt",
			"Gigaington", "Gigglybion", "Gingerryza", "GingerUoutLatina", "GodHinch", "Godmotoracy", "GodzillaSkunky",
			"Goersafe", "Goizedit", "GoobleTrimble", "GoodXglossy", "GoofyStud", "Gossiperit", "Grindereddi",
			"Grinderfl", "Grouplinkey", "Grumixty", "Guruncoads", "GutsyMud", "Hannali", "HappyVengeance",
			"HartSoundRoyal", "Headlinewa", "Healtif", "HeartHott", "Hehemccape", "Heimersc", "Helpfulsalv",
			"HipurMonster", "Hirschb", "Holyseedin", "Hondaliason", "HoopCent", "HoopGodzilla", "Hugzhotype",
			"Ineedigre", "Inentret", "Infocommu", "InformationShow", "Informerfi", "Injetase", "InloveMars",
			"InteriorKool", "InterviewBlab", "Intexoft", "InvaderDean", "JameWise", "Jawfins", "JinTaraDream",
			"Joancetaff", "Judianna", "Kaplicle", "KatrTinker", "Kavengisc", "Kavincor", "KeepupHelp", "Keyerson",
			"KinoBee", "KinoWunder", "KuroGlossy", "Ladynadiap", "Landric", "Lannecdo", "LastingChase", "Lativens",
			"Lawsurelful", "LeadXxActive", "Leplangi", "LifeExec", "Ligental", "LimeSanSolid", "LingBillion",
			"LipsxAlly", "ListKenjiChamp", "Litemark", "Liuminte", "Logicertarq", "LogicFallsGrim", "Lolandmara",
			"Loupertse", "LovesHot", "LunaticRappa", "LyfeSan", "Mafianite", "MagazineSmg", "MamaXcaptain",
			"ManiakAdvice", "Manselitra", "Mastens", "Maximereno", "Maxonymia", "Mercytelink", "MessagesWaves",
			"MewGree", "Microadep", "Midnightel", "Mindidonos", "MintInlove", "Mipollie", "Missimood", "MissIssue",
			"2hotConspiracy", "AboutWillow", "Achiena", "AlphaCert", "Andleysi", "AngelBoltSerene", "AngelTicker",
			"AnhartUpfor", "Animentex", "Annivet", "Annonhicsig", "Apoletso", "Aquaticalso", "Areechlu",
			"AshHartValued", "Astrating", "Badglobelyt", "BagoShin", "Bagword", "Basetechba", "BeastHiro", "Bestequake",
			"Billixisti", "Bipyrsuzl", "Birapher", "Blackencyse", "BlazeLucyMagazine", "Bleakovas", "Blemptis",
			"BlinkMoFried", "Blinknone", "BlondieVirtuoso", "BoardinJournal", "BorgReport", "Brainykvar", "Brainysarc",
			"Brideola", "Broadwayla", "Broodoplent", "BuggyWave", "Bunnykorepe", "Bushterro", "CaptainTheevil",
			"Carmicrysiq", "Carynes", "CasualAholic", "CasualShow", "Cationix", "Cavemoget", "Centektrex", "Centradem",
			"ChampionRox", "ChanMoon", "ChanZippo", "ChatCrazii", "Chayonnes", "Chiaricore", "ChronosDas",
			"ChronosSlayer", "Churradd", "Cipoliaen", "Climbran", "Cloniaz", "Cobramann", "Comedomes",
			"CommuniqueBliki", "ContentLeventis", "CookyRock", "Coratetr", "Coresman", "Corrics", "Countrabb",
			"CoverGlossy", "CrayonBuffPuff", "Crisphesi", "Cutielderce", "Cyclind", "CzarPark", "Dashicortwe",
			"DavIteFresh", "DeckLatina", "Delilli", "DemonBigg", "DeskCyber", "Dityxpre", "Dongedge", "DoodleVintage",
			"Downeesher", "Downetouth", "DravenNotice", "DrCotton", "Dyantech", "EarJung", "Easyodlexi", "Eatsendran",
			"EdgyHonda", "EnjoyVirtuoso", "Eprepti", "Etinetha", "Explisman", "Mochawk", "MoCookyIcy", "MomNot",
			"Motockerve", "Mourati", "MowerLasting", "MsVultureTight", "Nadisbc", "NeatPerfect", "Neopresumet",
			"Nersedix", "Netrolasp", "Nicerexos", "Nistrepe", "Nodatact", "Nosimber", "Numerymosyn", "Nuumnugo",
			"Onetellow", "Pandweak", "PeakYounger", "Perryla", "PhreekSand", "PiraAngelic", "PodTruck", "PongHartMunde",
			"Ponympayer", "Portiati", "Powerbrenol", "Preciseides", "Prentysto", "ProdigyPersonal", "Prosprian",
			"Quickermi", "Quillerga", "Raetstasebo", "Rassallr", "RavenNothing", "Reamgage", "Rebegged", "Recipetran",
			"ReggaeSan", "Reportersa", "RichFairyYau", "Richnitian", "Riderenst", "Robbick", "Roidelar", "Ronziasonc",
			"Royaldoub", "RozEliteHaro", "Rushpolt", "Screecher", "SelfBlackJournal", "Seuouser", "ShaySumoShopping",
			"ShinAnguris", "Simonworl", "Sistertegri", "Skatelymond", "Skylatchb", "Slypercerso", "Snooptiahau",
			"Snowermers", "SnowTriteTary", "SolidTeenzHan", "SpiderTwit", "SportsCrayon", "SpunkySupreme",
			"SummersBrainy", "", "FallsLess", "FangMedium", "FashionTen", "Feedsmill", "Fighteriera", "Fishamer",
			"Fixtulandie", "FleaPurple", "Fledcali", "Fonepler", "ForbHunter", "Foreverterb", "Formalth", "Fortunemole",
			"FreakSand", "Gambithbox", "Gamerisa", "GameRocker", "Gamertypern", "Genomypo", "Gentenbl", "GigglyYoung",
			"Ginowirett", "Gironati", "Giseler", "Glassione", "GlossyWilFizz", "GoDipity", "Golocarl", "GoodStyx",
			"Grabstoni", "Greetroot", "Grishine", "Groovider", "Groupposubs", "GrundyAniSlip", "GrundyChick",
			"Guantorian", "Guantoybri", "Gumship", "Harouskoli", "Heatusvi", "Heetahid", "Helpfulsi", "Heralthor",
			"HeroExoticSolid", "HeroWannaDeck", "Highteder", "Hilerati", "Hipertzero", "HockeyPrecise", "Hodderpi",
			"HumanAnhart", "Hummicon", "Huntalfo", "HunterLovely", "Ifallerer", "IfallExclusive", "Ifallsore",
			"Impexecte", "Inritsco", "Instand", "Intcattara", "Intrialis", "Intrify", "Invaderphar", "Invicor",
			"Islandermo", "IwantLil", "Jacketch", "Jimmythismo", "Joelvect", "Joinergi", "JoshYourTinnys",
			"Juzterstler", "Kailonec", "Karyanis", "KatChicFox", "Kendumac", "Keyrecom", "Kinodinary", "Klughnaddy",
			"Kurisuppet", "LaughRpgWorman", "Leravice", "Lijitas", "Lingeter", "LinHipurTips", "Linosoft",
			"ListDoodleBuddy", "LiveLove", "Logiconte", "LogicSlyDiscover", "LogicStroons", "LogPrank", "LolHenry",
			"LucyMoffWubba", "Luketsoneb", "Lyferrionb", "Macherad", "Macrower", "MagazineWaka", "Maidmerlev",
			"Maimissa", "MamaBliki", "Mamansenhi", "Marientum", "MarkAudience", "Markingb", "Mdoggernexm", "Melamyl",
			"MellowZapWolfie", "Minteroxim", "Miraclepc", "MissingTwilight", "Achipsyc", "Acomnics", "Acquiry",
			"Activendan", "Aeolisfi", "Affring", "Agilingli", "Aholickiku", "Ahonstra", "Aimerixsthe", "Alavans",
			"AlliHott", "Alogials", "AloneMarcs", "Amyoticew", "AngelFuzzy", "AngelicBradley", "Angurisapod",
			"AnimeValued", "Annikin", "AprilBest", "Artsellet", "Asialate", "AudienceTrevor", "Aughence", "Autotti",
			"Avelingsk", "Avertics", "Avignate", "Badingnor", "Ballabcon", "Balletonal", "BauerDailies", "BauerHomey",
			"Bazepped", "BeastDeluxe", "BeDas", "Beefessende", "Besterudwa", "Bestfmankt", "Biancept", "BiggInferno",
			"BinderWacky", "Birdmark", "BitBraceBorn", "BizarreBright", "BlabBoa", "BlazeYau", "Blonderlen", "Bluekirt",
			"Bohorthum", "Bollogyra", "BooshBabe", "BooshGalCyber", "Boostryzadd", "BoxKaven", "Bradleynera",
			"Bromondsp", "Buysantax", "Capeshla", "Captainista", "Carevega", "Carribe", "Catharmeral", "Cenconn",
			"Cheelcapod", "Chestomix", "ChirpQuayle", "Choreat", "ChosenGal", "CincoThedevil", "Cistepingh", "Clarnste",
			"ClearMark", "CleverShang", "Clevertame", "Coelock", "Colommsta", "Comerette", "Compunwi", "Conecmann",
			"Conesco", "ConspiracyCrash", "Consureak", "ContentWelLady", "CoverageWeb", "CrayonRapPro", "Crispeasiti",
			"CrownTales", "Crunchryon", "CuddlyFrogRappa", "DailiesFreezing", "Dailieste", "Danelissa",
			"Darth2cuteInvent", "Darthumna", "Dashedvailo", "DigestDarkPanet", "Dittspen", "DollAdvice", "Doublespeo",
			"Dramaineter", "Dramayonn", "DravenYuiPat", "Dreamyocom", "Drummersing", "EarBeastGlace", "EarPassion",
			"EatMud", "Ecablevu", "Elifera", "Elkaysupe", "Eloanie", "Enainsus", "Enexeche", "EpicMoLover", "Equardom",
			"Esarwise", "Euriculli", "ExclusiveKino", "Exoticleux", "ExtraGambit", "Mobidade", "MoffAraWiz",
			"Monoemtek", "Movieldicel", "Mozapsyi", "Muckbase", "MundeFallen", "Murayamoto", "Murphysion", "Natholeyet",
			"Ndevrakin", "Nettenex", "NicerUnow", "Nobingen", "Norweraco", "NotDance", "Number1we", "NycVital",
			"Oardiant", "Oblekoin", "Ocotelso", "OmahaRainbow", "Onlinesvil", "Oxpearli", "PeatearBelGossip",
			"PhatBulletin", "PhoenixSporty", "Pierconca", "Pinchlord", "Planettapa", "Plasiali", "Playerskin",
			"PleasantYes", "Pongosison", "Powerbrenol", "Poweressans", "Prectrco", "Preperator", "Preprongre",
			"Prepylahoe", "Presseyan", "Prolevertme", "Prouderwe", "RacerCincoVintage", "RadiantTopics", "Readtenzol",
			"ReadyCandy", "Rheembrig", "Ridabaughs", "RollGroupActually", "Rollianoff", "RomanceGossip", "Rosescale",
			"RozMr", "Sandetiz", "ScanMon", "Seldeedo", "Shanghyte", "ShatUnow", "SlayerFun", "SlimChi", "Soliviat",
			"SpyderMon");
	private static final Random rand = new Random();

	private static String getRandomNameFromList() {
		return names.get(rand.nextInt(names.size()));
	}

	private static final String numbers = "0123456789";

	private static String addNumbers(String name) {
		StringBuilder builder = new StringBuilder(name);
		for (int i = 0; i < rand.nextInt(4); i++) {
			builder.append(numbers.charAt(rand.nextInt(numbers.length())));
		}
		return builder.toString();
	}

	private static String upperCaseRandom(String input, int n) {
		final int length = input.length();
		final StringBuilder output = new StringBuilder(input);
		final boolean[] alreadyChecked = new boolean[length];
		final Random r = new Random();

		for (int i = 0, checks = 0; i < n && checks < length; i++) {
			// Pick a place
			int position = r.nextInt(length);

			// Check if lowercase alpha
			if (!alreadyChecked[position]) {
				if (Character.isLowerCase(output.charAt(position))) {
					output.setCharAt(position, Character.toUpperCase(output.charAt(position)));
				}
				else {
					i--;
				}
				checks++;
				alreadyChecked[position] = true;
			}
			else {
				i--;
			}
		}
		return output.toString();
	}

	public static String getRandomName() {
		String name;
		do {
			name = randomName();
		}
		while (name.length() > 16 || name.length() < 2);
		return name;
	}

	private static String randomName() {
		String nameFromList = getRandomNameFromList();
		String randomCase = upperCaseRandom(nameFromList, rand.nextInt(4));
		String withNumbers = addNumbers(randomCase);
		return withNumbers;
	}
}

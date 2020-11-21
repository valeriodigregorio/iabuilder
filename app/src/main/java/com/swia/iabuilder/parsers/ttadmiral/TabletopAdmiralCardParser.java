package com.swia.iabuilder.parsers.ttadmiral;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.utils.CardUtils;
import com.swia.iabuilder.parsers.CardParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabletopAdmiralCardParser implements CardParser<Integer> {

    private static final int TTA_FIRST_IACP_DEPLOYMENT_CARD = 175;
    private static final int TTA_FIRST_IACP_COMMAND_CARD = 233;

    private static final Map<CardSystem, Map<CardType, Map<Integer, Integer>>> ToTTA = new HashMap<>();
    private static final Map<CardType, Map<Integer, Integer>> FromTTA = new HashMap<>();

    static {
        Map<Integer, Integer> map;
        for (CardSystem cardSystem : CardSystem.values()) {
            Map<CardType, Map<Integer, Integer>> to = new HashMap<>();
            for (CardType cardType : CardUtils.VISIBLE_CARD_TYPES) {
                to.put(cardType, new HashMap<>());
                FromTTA.put(cardType, new HashMap<>());
            }
            ToTTA.put(cardSystem, to);
        }

        // FFG - DEPLOYMENT CARDS: IAB => TTA
        {
            map = ToTTA.get(CardSystem.FFG).get(CardType.DEPLOYMENT);
            map.put(0, 133); // 0-0-0
            map.put(1, 18); // AT-ST [Elite]
            map.put(2, 117); // Advanced Com Systems
            map.put(4, 7); // Agent Blaise
            map.put(5, 9); // Alliance Ranger
            map.put(6, 8); // Alliance Ranger [Elite]
            map.put(7, 11); // Alliance Smuggler
            map.put(8, 10); // Alliance Smuggler [Elite]
            map.put(9, 131); // BT-1
            map.put(10, 99); // Balance of the Force
            map.put(11, 12); // Bantha Rider [Elite]
            map.put(12, 95); // Beast Tamer
            map.put(13, 84); // Biv Bodhrik
            map.put(14, 121); // Black Market
            map.put(15, 13); // Boba Fett
            map.put(16, 14); // Bossk
            map.put(18, 62); // C-3PO
            map.put(19, 132); // C1-10P
            map.put(20, 15); // Captain Terro
            map.put(21, 114); // Channel the Force
            map.put(22, 17); // Chewbacca
            map.put(23, 107); // Combat Suit
            map.put(24, 111); // Cross-Training
            map.put(25, 19); // Darth Vader
            map.put(26, 77); // Davith Elso
            map.put(27, 37); // Dengar
            map.put(28, 100); // Devious Scheme
            map.put(29, 16); // Dewback Rider [Elite]
            map.put(30, 20); // Diala Passil
            map.put(31, 22); // E-Web Engineer
            map.put(32, 21); // E-Web Engineer [Elite]
            map.put(33, 39); // Echo Base Trooper
            map.put(34, 38); // Echo Base Trooper [Elite]
            map.put(35, 96); // Explosive Armaments
            map.put(36, 98); // Feeding Frenzy
            map.put(37, 23); // Fenn Signis
            map.put(38, 101); // First Strike
            map.put(39, 135); // Focused on the Kill
            map.put(40, 118); // Fury of Kashyyyk
            map.put(41, 24); // Gaarkhan
            map.put(42, 48); // Gamorrean Guard
            map.put(43, 47); // Gamorrean Guard [Elite]
            map.put(44, 40); // General Sorin
            map.put(45, 6); // General Weiss
            map.put(46, 5); // Gideon Argus
            map.put(47, 41); // Greedo
            map.put(48, 67); // HK Assassin Droid
            map.put(49, 66); // HK Assassin Droid [Elite]
            map.put(50, 2); // Han Solo
            map.put(51, 97); // Headhunter
            map.put(52, 86); // Heavy Stormtrooper
            map.put(53, 85); // Heavy Stormtrooper [Elite]
            map.put(54, 129); // Hera Syndulla
            map.put(55, 122); // Heroic Effort
            map.put(56, 43); // Hired Gun
            map.put(57, 42); // Hired Gun [Elite]
            map.put(58, 44); // IG-88
            map.put(59, 46); // ISB Infiltrator
            map.put(60, 45); // ISB Infiltrator [Elite]
            map.put(61, 26); // Imperial Officer
            map.put(62, 25); // Imperial Officer [Elite]
            map.put(63, 124); // Indentured Jester
            map.put(65, 120); // Jabba the Hutt
            map.put(66, 134); // Jawa Scavenger
            map.put(68, 136); // Jawa Scavenger [Elite]
            map.put(69, 50); // Jet Trooper
            map.put(70, 49); // Jet Trooper [Elite]
            map.put(71, 27); // Jyn Odan
            map.put(72, 57); // Kayn Somos
            map.put(73, 58); // Lando Calrissian
            map.put(74, 110); // Last Resort
            map.put(76, 59); // Leia Organa
            map.put(77, 68); // Loku Kanoloa
            map.put(78, 1); // Luke Skywalker (Hero of the Rebellion)
            map.put(79, 60); // Luke Skywalker (Jedi Knight)
            map.put(80, 69); // MHD-19
            map.put(81, 28); // Mak Eshka'rey
            map.put(82, 128); // Motivation
            map.put(83, 78); // Murne Rin
            map.put(84, 30); // Nexu
            map.put(85, 29); // Nexu [Elite]
            map.put(86, 61); // Obi-Wan Kenobi
            map.put(88, 113); // On a Diplomatic Mission
            map.put(89, 51); // Onar Koma
            map.put(90, 109); // Prey on the Weak
            map.put(91, 31); // Probe Droid
            map.put(92, 123); // Probe Droid [Elite]
            map.put(93, 106); // Punishing Strike
            map.put(95, 63); // R2-D2
            map.put(97, 52); // Rancor [Elite]
            map.put(98, 102); // Rebel High Command
            map.put(99, 64); // Rebel Saboteur
            map.put(100, 3); // Rebel Saboteur [Elite]
            map.put(101, 65); // Rebel Trooper
            map.put(102, 4); // Rebel Trooper [Elite]
            map.put(103, 32); // Royal Guard
            map.put(104, 125); // Royal Guard [Elite]
            map.put(105, 76); // Royal Guard Champion
            map.put(106, 112); // Rule by Fear
            map.put(107, 70); // SC2-M Repulsor Tank [Elite]
            map.put(108, 87); // Saska Teft
            map.put(109, 130); // Scavenged Weaponry
            map.put(110, 53); // Shyla Varad
            map.put(111, 94); // Smuggler's Run
            map.put(112, 72); // Snowtrooper
            map.put(113, 71); // Snowtrooper [Elite]
            map.put(114, 34); // Stormtrooper
            map.put(115, 33); // Stormtrooper [Elite]
            map.put(116, 93); // Survivalist
            map.put(117, 115); // Targeting Computer
            map.put(118, 104); // Mercenary Temporary Alliance
            map.put(119, 103); // Imperial Temporary Alliance
            map.put(120, 108); // The General's Ranks
            map.put(121, 83); // The Grand Inquisitor
            map.put(122, 36); // Trandoshan Hunter
            map.put(123, 35); // Trandoshan Hunter [Elite]
            map.put(124, 143); // Trusted Ally
            map.put(125, 89); // Tusken Raider
            map.put(126, 88); // Tusken Raider [Elite]
            map.put(127, 80); // Ugnaught Tinkerer
            map.put(128, 79); // Ugnaught Tinkerer [Elite]
            map.put(129, 116); // Under Duress
            map.put(130, 126); // Unshakable
            map.put(131, 105); // Vader's Finest
            map.put(132, 73); // Verena Talos
            map.put(133, 54); // Vinto Hreeda
            map.put(134, 75); // Wampa
            map.put(135, 74); // Wampa [Elite]
            map.put(136, 56); // Weequay Pirate
            map.put(137, 55); // Weequay Pirate [Elite]
            map.put(138, 82); // Wing Guard
            map.put(139, 81); // Wing Guard [Elite]
            map.put(140, 91); // Wookiee Warrior
            map.put(141, 90); // Wookiee Warrior [Elite]
            map.put(142, 92); // Zillo Technique
            map.put(143, 144); // AT-DP [Elite]
            map.put(144, 142); // Ahsoka Tano
            map.put(145, 146); // Clawdite Shapeshifter
            map.put(146, 152); // Clawdite Shapeshifter [Elite]
            map.put(147, 139); // Driven by Hatred
            map.put(148, 147); // Drokkatta
            map.put(149, 141); // Emperor Palpatine
            map.put(150, 148); // Jarrod Kelvin
            map.put(151, 138); // Ko-Tun Feralo
            map.put(152, 140); // Maul
            map.put(154, 151); // Riot Trooper
            map.put(156, 153); // Riot Trooper [Elite]
            map.put(157, 149); // Rogue Smuggler
            map.put(158, 145); // Sentry Droid
            map.put(159, 154); // Sentry Droid [Elite]
            map.put(160, 150); // Wookiee Avenger
            map.put(162, 173); // Thrawn
            map.put(164, 162); // Death Trooper
            map.put(166, 163); // Death Trooper [Elite]
            map.put(167, 160); // Loth-cat
            map.put(168, 159); // Loth-cat [Elite]
            map.put(170, 170); // Hondo Ohnaka
            map.put(171, 161); // CT-1701
            map.put(172, 158); // Tress Hacnua
            map.put(173, 172); // Spectre Cell
            map.put(174, 157); // Zeb Orrelios
            map.put(176, 165); // Ezra Bridger
            map.put(178, 164); // Kanan Jarrus
            map.put(180, 167); // Sabine Wren
            map.put(181, 174); // Heavy Fire
            map.put(182, 171); // Lie in Ambush
            map.put(183, 169); // Extra Armor
            map.put(184, 166); // Doubt
        }

        // IACP - DEPLOYMENT CARDS: IAB => TTA
        {
            map = ToTTA.get(CardSystem.IACP).get(CardType.DEPLOYMENT);
            map.putAll(ToTTA.get(CardSystem.FFG).get(CardType.DEPLOYMENT));
            map.put(1, 197); // AT-ST [Elite] (IACP)
            map.put(2, 196); // Advanced Com Systems (IACP)
            map.put(9, 176); // BT-1 (IACP)
            map.put(13, 210); // Biv Bodhrik (IACP)
            map.put(15, 198); // Boba Fett (IACP)
            map.put(27, 211); // Dengar (IACP)
            map.put(30, 177); // Diala Passil (IACP)
            map.put(34, 178); // Echo Base Trooper [Elite] (IACP)
            map.put(37, 201); // Fenn Signis (IACP)
            //map.put(40, 213); -- REMOVED -- // Fury of Kashyyyk (IACP)
            map.put(41, 202); // Gaarkhan (IACP)
            map.put(43, 179); // Gamorrean Guard [Elite] (IACP)
            map.put(45, 203); // General Weiss (IACP)
            map.put(71, 205); // Jyn Odan (IACP)
            map.put(72, 181); // Kayn Somos (IACP)
            map.put(76, 183); // Leia Organa (IACP)
            map.put(78, 184); // Luke Skywalker (Hero of the Rebellion) (IACP)
            map.put(79, 185); // Luke Skywalker (Jedi Knight) (IACP)
            map.put(86, 214); // Obi-Wan Kenobi (IACP)
            map.put(92, 187); // Probe Droid [Elite] (IACP)
            map.put(97, 188); // Rancor [Elite] (IACP)
            map.put(102, 189); // Rebel Trooper [Elite] (IACP)
            map.put(104, 215); // Royal Guard [Elite] (IACP)
            map.put(105, 207); // Royal Guard Champion (IACP)
            map.put(107, 217); // SC2-M Repulsor Tank [Elite] (IACP)
            map.put(108, 216); // Saska Teft (IACP)
            map.put(110, 191); // Shyla Varad (IACP)
            map.put(115, 192); // Stormtrooper [Elite] (IACP)
            map.put(121, 193); // The Grand Inquisitor (IACP)
            map.put(126, 220); // Tusken Raider [Elite] (IACP)
            map.put(139, 194); // Wing Guard [Elite] (IACP)
            map.put(141, 195); // Wookiee Warrior [Elite] (IACP)
            map.put(144, 175); // Ahsoka Tano (IACP)
            map.put(151, 182); // Ko-Tun Feralo (IACP)
            map.put(152, 186); // Maul (IACP)
            map.put(159, 190); // Sentry Droid [Elite] (IACP)
            map.put(178, 180); // Kanan Jarrus (IACP)
            map.put(185, 204); // Imperial Retrofitting (IACP)
            //map.put(186, 200); -- REMOVED -- // Cohesive Fireteam (IACP)
            map.put(187, 208); // Suppressive Fire (IACP)
            map.put(188, 219); // Yoda (IACP)
            map.put(189, 218); // Scout Trooper (IACP)
            map.put(190, 209); // 4-LOM (IACP)
            map.put(191, 212); // Zuckuss (IACP)
            map.put(16, 224); // Bossk (IACP)
            map.put(32, 231); // E-Web Engineer [Elite]
            map.put(44, 232); // General Sorin (IACP)
            map.put(192, 227); // IG-11 (IACP)
            map.put(73, 233); // Lando Calrissian (IACP)
            map.put(77, 234); // Loku Kanoloa (IACP)
            map.put(193, 228); // The Mandalorian (IACP)
            map.put(123, 241); // Trandoshan Hunter [Elite] (IACP)
            map.put(194, 237); // Overwatch (IACP)
            map.put(195, 230); // Clan of Two (IACP)
            map.put(196, 235); // Shoretrooper [Elite] (IACP)
            map.put(197, 236); // Mortar Trooper [Elite] (IACP)
            map.put(198, 223); // AT-RT [Elite] (IACP)
            map.put(100, 239); // Rebel Saboteur [Elite] (IACP)
            map.put(200, 226); // Director Krennic (IACP)
            map.put(201, 225); // Jyn Erso (IACP)
            map.put(202, 240); // The Darksaber (IACP)
            map.put(4, 222); // Agent Blaise (IACP)
            map.put(60, 221); // ISB Infiltrator [Elite] (IACP)
            map.put(203, 238); // Rebel Pathfinder [Elite] (IACP)
            map.put(132, 242); // Verena Talos (IACP)
            map.put(204, 243); // Cassian Andor (IACP)
            map.put(205, 244); // K-2SO (IACP)
        }

        // FFG - COMMAND CARDS: IAB => TTA
        {
            map = ToTTA.get(CardSystem.FFG).get(CardType.COMMAND);
            map.put(0, 98); // A Powerful Influence
            map.put(1, 17); // Adrenaline
            map.put(2, 177); // Advance Warning
            map.put(3, 61); // Against the Odds
            map.put(4, 143); // Assassinate
            map.put(5, 170); // Ballistics Matrix
            map.put(6, 151); // Battlefield Awareness
            map.put(7, 95); // Behind Enemy Lines
            map.put(8, 92); // Black Market Prices
            map.put(9, 166); // Bladestorm
            map.put(10, 81); // Blaze of Glory
            map.put(11, 154); // Blitz
            map.put(12, 144); // Blood Feud
            map.put(13, 124); // Bodyguard
            map.put(14, 58); // Brace Yourself
            map.put(15, 182); // Brace for Impact
            map.put(16, 22); // Burst Fire
            map.put(17, 148); // Call the Vanguard
            map.put(18, 130); // Camouflage
            map.put(19, 12); // Capture the Weary
            map.put(20, 152); // Cavalry Charge
            map.put(21, 23); // Celebration
            map.put(22, 106); // Change of Plans
            map.put(23, 93); // Cheat to Win
            map.put(24, 24); // Close the Gap
            map.put(25, 107); // Collect Intel
            map.put(26, 86); // Comm Disruption
            map.put(27, 115); // Coordinated Attack
            map.put(28, 125); // Counter Attack
            map.put(29, 25); // Covering Fire
            map.put(30, 126); // Cripple
            map.put(31, 137); // Cruel Strike
            map.put(32, 8); // Crush
            map.put(33, 181); // Cut Lines
            map.put(34, 59); // Dangerous Bargains
            map.put(35, 87); // Data Theft
            map.put(36, 26); // Deadeye
            map.put(37, 134); // Deadly Precision
            map.put(38, 18); // Debts Repaid
            map.put(39, 27); // Deflection
            map.put(40, 101); // Devotion
            map.put(41, 82); // Dirty Trick
            map.put(42, 75); // Disable
            map.put(43, 14); // Disorient
            map.put(44, 164); // Draw!
            map.put(45, 174); // Eerie Visage
            map.put(46, 62); // Efficient Travel
            map.put(47, 28); // Element of Surprise
            map.put(48, 108); // Emergency Aid
            map.put(49, 67); // Endless Reserves
            map.put(50, 4); // Espionage Mastery
            map.put(51, 102); // Etiquette and Protocol
            map.put(52, 179); // Evacuate
            map.put(53, 68); // Explosive Weaponry
            map.put(54, 29); // Expose Weakness
            map.put(55, 165); // Extra Protection
            map.put(56, 131); // Fatal Deception
            map.put(57, 153); // Feral Swipes
            map.put(58, 30); // Ferocity
            map.put(59, 64); // Field Tactician
            map.put(60, 31); // Fleet Footed
            map.put(61, 127); // Flurry of Blades
            map.put(62, 111); // Focus
            map.put(63, 132); // Force Illusion
            map.put(64, 32); // Force Lightning
            map.put(65, 135); // Force Rush
            map.put(66, 99); // Force Surge
            map.put(67, 65); // Fuel Upgrade
            map.put(68, 33); // Furious Charge
            map.put(69, 162); // Glory of the Kill
            map.put(70, 112); // Grenadier
            map.put(71, 15); // Grisly Contest
            map.put(72, 34); // Guardian Stance
            map.put(73, 103); // Hard to Hit
            map.put(74, 116); // Harsh Environment
            map.put(75, 1); // Heart of Freedom
            map.put(76, 69); // Heavy Armor
            map.put(77, 149); // Heightened Reflexes
            map.put(78, 138); // Hidden Trap
            map.put(79, 109); // Hide in Plain Sight
            map.put(80, 63); // Hit and Run
            map.put(81, 19); // Hold Ground
            map.put(82, 136); // Hunt Them Down
            map.put(83, 35); // Hunter Protocol
            map.put(84, 96); // I Can Feel It
            map.put(85, 100); // I Must Go Alone
            map.put(86, 76); // I Make My Own Luck
            map.put(87, 140); // Improvised Weapons
            map.put(88, 72); // In the Shadows
            map.put(89, 77); // Inspiring Speech
            map.put(90, 5); // Intelligence Leak
            map.put(91, 145); // Jump Jets
            map.put(92, 9); // Jundland Terror
            map.put(93, 36); // Knowledge and Defense
            map.put(94, 110); // Lock On
            map.put(95, 37); // Lord of the Sith
            map.put(96, 38); // Lure of the Dark Side
            map.put(97, 13); // Mandalorian Tactics
            map.put(98, 39); // Marksman
            map.put(99, 117); // Master Operative
            map.put(100, 40); // Maximum Firepower
            map.put(101, 41); // Meditation
            map.put(102, 83); // Merciless
            map.put(103, 118); // Miracle Worker
            map.put(104, 150); // Mitigate
            map.put(105, 176); // Navigation Upgrade
            map.put(106, 119); // Negation
            map.put(107, 70); // New Orders
            map.put(108, 80); // Of No Importance
            map.put(109, 172); // On a Mission
            map.put(110, 157); // On the Lam
            map.put(111, 10); // Opportunistic
            map.put(112, 66); // Optimal Bombardment
            map.put(113, 120); // Overcharged Weapons
            map.put(114, 84); // Overdrive
            map.put(115, 71); // Overrun
            map.put(116, 155); // Parry
            map.put(117, 11); // Parting Blow
            map.put(118, 60); // Payback
            map.put(119, 160); // Pickpocket
            map.put(120, 156); // Positioning Advantage
            map.put(121, 73); // Primary Target
            map.put(122, 168); // Provoke
            map.put(123, 89); // Rally the Troops
            map.put(124, 90); // Rank and File
            map.put(125, 113); // Reinforcements
            map.put(126, 85); // Repair
            map.put(127, 78); // Reposition
            map.put(128, 20); // Roar
            map.put(129, 163); // Run for Cover
            map.put(130, 2); // Self-Defense
            map.put(131, 128); // Set for Stun
            map.put(132, 121); // Set a Trap
            map.put(133, 169); // Shared Experience
            map.put(134, 142); // Shoot the Messenger
            map.put(135, 104); // Single Purpose
            map.put(136, 122); // Size Advantage
            map.put(137, 79); // Slippery Target
            map.put(138, 6); // Smuggled Supplies
            map.put(139, 7); // Smugglers Tricks
            map.put(140, 91); // Squad Swarm
            map.put(141, 94); // Stall for Time
            map.put(142, 139); // Stay Down
            map.put(143, 129); // Stealth Tactics
            map.put(144, 159); // Stimulants
            map.put(145, 88); // Strategic Shift
            map.put(146, 114); // Strength in Numbers
            map.put(147, 74); // Stroke of Brilliance
            map.put(148, 123); // Survival Instincts
            map.put(149, 183); // Take Position
            map.put(150, 178); // Targeting Network
            map.put(151, 105); // Terminal Network
            map.put(152, 189); // Terminal Protocol
            map.put(153, 97); // There is Another
            map.put(154, 158); // Tools for the Job
            map.put(155, 146); // Tough Luck
            map.put(156, 161); // Toxic Dart
            map.put(157, 16); // Trandoshan Terror
            map.put(158, 171); // Triangulate
            map.put(159, 175); // Utinni!
            map.put(160, 133); // Vanish
            map.put(161, 147); // Wild Attack
            map.put(162, 141); // Wild Fury
            map.put(163, 21); // Wookiee Rage
            map.put(164, 205); // Arcing Shot
            map.put(165, 206); // Armed Escort
            map.put(174, 191); // Balancing Force
            map.put(166, 184); // Battle Scars
            map.put(184, 196); // Chaotic Force
            map.put(167, 185); // Collateral Damage
            map.put(168, 207); // Concentrated Fire
            map.put(179, 201); // Corrupting Force
            map.put(180, 202); // Dark Energy
            map.put(185, 197); // Deathblow
            map.put(169, 208); // Droid Mastery
            map.put(186, 198); // Face to Face
            map.put(170, 186); // Field Supply
            map.put(175, 192); // Fool Me Once
            map.put(176, 193); // Force Jump
            map.put(177, 194); // Force Push
            map.put(171, 209); // Forward March
            map.put(172, 187); // Heavy Ordnance
            map.put(187, 180); // Looking for a Fight
            map.put(181, 210); // Officer's Training
            map.put(182, 203); // Prepared for Battle
            map.put(173, 188); // Ready Weapons
            map.put(178, 195); // Right Back At Ya!
            map.put(183, 204); // Unlimited Power
            map.put(188, 200); // Wreak Vengeance
            map.put(189, 42); // One in a Million
            map.put(190, 43); // Planning
            map.put(191, 44); // Price on Their Heads
            map.put(192, 45); // Pummel
            map.put(193, 46); // Rally
            map.put(194, 47); // Recovery
            map.put(195, 48); // Regroup
            map.put(196, 167); // Sarlacc Sweep
            map.put(197, 49); // Shadow Ops
            map.put(198, 50); // Sit Tight
            map.put(199, 51); // Son of Skywalker
            map.put(200, 52); // Take Cover
            map.put(201, 53); // Take Initiative
            map.put(202, 54); // Take it Down
            map.put(203, 55); // Telekinetic Throw
            map.put(204, 56); // To the Limit
            map.put(205, 57); // Urgency
            map.put(206, 231); // Combat Resupply
            map.put(207, 218); // Dying Lunge
            map.put(208, 232); // Escalating Hostility
            map.put(209, 230); // Foresee
            map.put(210, 222); // Hostile Negotiation
            map.put(211, 217); // Hour of Need
            map.put(212, 228); // Induce Rage
            map.put(213, 219); // Karabast
            map.put(214, 216); // Learn by Example
            map.put(215, 223); // Lets Make a Deal
            map.put(216, 226); // Marked Territory
            map.put(217, 224); // Out of Time
            map.put(218, 227); // Pack Alpha
            map.put(219, 229); // Price of Glory
            map.put(220, 215); // Protect the Old Ways
            map.put(221, 220); // Rebel Graffiti
            map.put(222, 214); // Second Chance
            map.put(223, 213); // Signal Jammer
            map.put(224, 211); // Spinning Kick
            map.put(225, 221); // Veteran Instincts
            map.put(226, 212); // Wild Fire
            map.put(227, 225); // Worth Every Credit
        }

        // IACP - COMMAND CARDS: IAB => TTA
        {
            map = ToTTA.get(CardSystem.IACP).get(CardType.COMMAND);
            map.putAll(ToTTA.get(CardSystem.FFG).get(CardType.COMMAND));
            map.put(4, 233); // Assassinate (IACP)
            map.put(39, 236); // Deflection (IACP)
            map.put(93, 239); // Knowledge and Defense (IACP)
            map.put(97, 238); // Mandalorian Tactics (IACP)
            map.put(110, 235); // On the Lam (IACP)
            map.put(116, 243); // Parry (IACP)
            map.put(228, 240); // Close and Personal (IACP)
            map.put(229, 241); // Findsman Meditation (IACP)
            map.put(230, 242); // Get Behind Me! (IACP)
            map.put(231, 244); // Preservation Protocol (IACP)
            //map.put(232, 245); -- REMOVED -- // Soften the Blow (IACP)
            map.put(232, 247); // Iron Will (IACP)
            map.put(233, 246); // There Is No Try (IACP)
            map.put(234, 250); // Guild Programming (IACP)
            map.put(235, 251); // Whistling Birds (IACP)
            map.put(236, 248); // Built on Hope (IACP)
            map.put(237, 249); // Deploy the Garrison! (IACP)
            map.put(238, 252); // Blend In (IACP)
        }

        // DEPLOYMENT CARDS: TTA => IAB
        {
            map = FromTTA.get(CardType.DEPLOYMENT);
            map.put(0, null); // None
            map.put(1, 78); // Luke Skywalker (Hero of the Rebellion)
            map.put(2, 50); // Han Solo
            map.put(3, 100); // Rebel Saboteur [Elite]
            map.put(4, 102); // Rebel Trooper [Elite]
            map.put(5, 46); // Gideon Argus
            map.put(6, 45); // General Weiss
            map.put(7, 4); // Agent Blaise
            map.put(8, 6); // Alliance Ranger [Elite]
            map.put(9, 5); // Alliance Ranger
            map.put(10, 8); // Alliance Smuggler [Elite]
            map.put(11, 7); // Alliance Smuggler
            map.put(12, 11); // Bantha Rider [Elite]
            map.put(13, 15); // Boba Fett
            map.put(14, 16); // Bossk
            map.put(15, 20); // Captain Terro
            map.put(16, 29); // Dewback Rider [Elite]
            map.put(17, 22); // Chewbacca
            map.put(18, 1); // AT-ST [Elite]
            map.put(19, 25); // Darth Vader
            map.put(20, 30); // Diala Passil
            map.put(21, 32); // E-Web Engineer [Elite]
            map.put(22, 31); // E-Web Engineer
            map.put(23, 37); // Fenn Signis
            map.put(24, 41); // Gaarkhan
            map.put(25, 62); // Imperial Officer [Elite]
            map.put(26, 61); // Imperial Officer
            map.put(27, 71); // Jyn Odan
            map.put(28, 81); // Mak Eshka'rey
            map.put(29, 85); // Nexu [Elite]
            map.put(30, 84); // Nexu
            map.put(31, 91); // Probe Droid
            map.put(32, 103); // Royal Guard
            map.put(33, 115); // Stormtrooper [Elite]
            map.put(34, 114); // Stormtrooper
            map.put(35, 123); // Trandoshan Hunter [Elite]
            map.put(36, 122); // Trandoshan Hunter
            map.put(37, 27); // Dengar
            map.put(38, 34); // Echo Base Trooper [Elite]
            map.put(39, 33); // Echo Base Trooper
            map.put(40, 44); // General Sorin
            map.put(41, 47); // Greedo
            map.put(42, 57); // Hired Gun [Elite]
            map.put(43, 56); // Hired Gun
            map.put(44, 58); // IG-88
            map.put(45, 60); // ISB Infiltrator [Elite]
            map.put(46, 59); // ISB Infiltrator
            map.put(47, 43); // Gamorrean Guard [Elite]
            map.put(48, 42); // Gamorrean Guard
            map.put(49, 70); // Jet Trooper [Elite]
            map.put(50, 69); // Jet Trooper
            map.put(51, 89); // Onar Koma
            map.put(52, 97); // Rancor [Elite]
            map.put(53, 110); // Shyla Varad
            map.put(54, 133); // Vinto Hreeda
            map.put(55, 137); // Weequay Pirate [Elite]
            map.put(56, 136); // Weequay Pirate
            map.put(57, 72); // Kayn Somos
            map.put(58, 73); // Lando Calrissian
            map.put(59, 76); // Leia Organa
            map.put(60, 79); // Luke Skywalker (Jedi Knight)
            map.put(61, 86); // Obi-Wan Kenobi
            map.put(62, 18); // C-3PO
            map.put(63, 95); // R2-D2
            map.put(64, 99); // Rebel Saboteur
            map.put(65, 101); // Rebel Trooper
            map.put(66, 49); // HK Assassin Droid [Elite]
            map.put(67, 48); // HK Assassin Droid
            map.put(68, 77); // Loku Kanoloa
            map.put(69, 80); // MHD-19
            map.put(70, 107); // SC2-M Repulsor Tank [Elite]
            map.put(71, 113); // Snowtrooper [Elite]
            map.put(72, 112); // Snowtrooper
            map.put(73, 132); // Verena Talos
            map.put(74, 135); // Wampa [Elite]
            map.put(75, 134); // Wampa
            map.put(76, 105); // Royal Guard Champion
            map.put(77, 26); // Davith Elso
            map.put(78, 83); // Murne Rin
            map.put(79, 128); // Ugnaught Tinkerer [Elite]
            map.put(80, 127); // Ugnaught Tinkerer
            map.put(81, 139); // Wing Guard [Elite]
            map.put(82, 138); // Wing Guard
            map.put(83, 121); // The Grand Inquisitor
            map.put(84, 13); // Biv Bodhrik
            map.put(85, 53); // Heavy Stormtrooper [Elite]
            map.put(86, 52); // Heavy Stormtrooper
            map.put(87, 108); // Saska Teft
            map.put(88, 126); // Tusken Raider [Elite]
            map.put(89, 125); // Tusken Raider
            map.put(90, 141); // Wookiee Warrior [Elite]
            map.put(91, 140); // Wookiee Warrior
            map.put(92, 142); // Zillo Technique
            map.put(93, 116); // Survivalist
            map.put(94, 111); // Smuggler's Run
            map.put(95, 12); // Beast Tamer
            map.put(96, 35); // Explosive Armaments
            map.put(97, 51); // Headhunter
            map.put(98, 36); // Feeding Frenzy
            map.put(99, 10); // Balance of the Force
            map.put(100, 28); // Devious Scheme
            map.put(101, 38); // First Strike
            map.put(102, 98); // Rebel High Command
            map.put(103, 119); // Imperial Temporary Alliance
            map.put(104, 118); // Mercenary Temporary Alliance
            map.put(105, 131); // Vader's Finest
            map.put(106, 93); // Punishing Strike
            map.put(107, 23); // Combat Suit
            map.put(108, 120); // The General's Ranks
            map.put(109, 90); // Prey on the Weak
            map.put(110, 74); // Last Resort
            map.put(111, 24); // Cross-Training
            map.put(112, 106); // Rule by Fear
            map.put(113, 88); // On a Diplomatic Mission
            map.put(114, 21); // Channel the Force
            map.put(115, 117); // Targeting Computer
            map.put(116, 129); // Under Duress
            map.put(117, 2); // Advanced Com Systems
            map.put(118, 40); // Fury of Kashyyyk
            map.put(120, 65); // Jabba the Hutt
            map.put(121, 14); // Black Market
            map.put(122, 55); // Heroic Effort
            map.put(123, 92); // Probe Droid [Elite]
            map.put(124, 63); // Indentured Jester
            map.put(125, 104); // Royal Guard [Elite]
            map.put(126, 130); // Unshakable
            map.put(128, 82); // Motivation
            map.put(129, 54); // Hera Syndulla
            map.put(130, 109); // Scavenged Weaponry
            map.put(131, 9); // BT-1
            map.put(132, 19); // C1-10P
            map.put(133, 0); // 0-0-0
            map.put(134, 66); // Jawa Scavenger
            map.put(135, 39); // Focused on the Kill
            map.put(136, 68); // Jawa Scavenger [Elite]
            map.put(138, 151); // Ko-Tun Feralo
            map.put(139, 147); // Driven by Hatred
            map.put(140, 152); // Maul
            map.put(141, 149); // Emperor Palpatine
            map.put(142, 144); // Ahsoka Tano
            map.put(143, 124); // Trusted Ally
            map.put(144, 143); // AT-DP [Elite]
            map.put(145, 158); // Sentry Droid
            map.put(146, 145); // Clawdite Shapeshifter
            map.put(147, 148); // Drokkatta
            map.put(148, 150); // Jarrod Kelvin
            map.put(149, 157); // Rogue Smuggler
            map.put(150, 160); // Wookiee Avenger
            map.put(151, 154); // Riot Trooper
            map.put(152, 146); // Clawdite Shapeshifter [Elite]
            map.put(153, 156); // Riot Trooper [Elite]
            map.put(154, 159); // Sentry Droid [Elite]
            map.put(157, 174); // Zeb Orrelios
            map.put(158, 172); // Tress Hacnua
            map.put(159, 168); // Loth-cat [Elite]
            map.put(160, 167); // Loth-cat
            map.put(161, 171); // CT-1701
            map.put(162, 164); // Death Trooper
            map.put(163, 166); // Death Trooper [Elite]
            map.put(164, 178); // Kanan Jarrus
            map.put(165, 176); // Ezra Bridger
            map.put(166, 184); // Doubt
            map.put(167, 180); // Sabine Wren
            map.put(169, 183); // Extra Armor
            map.put(170, 170); // Hondo Ohnaka
            map.put(171, 182); // Lie in Ambush
            map.put(172, 173); // Spectre Cell
            map.put(173, 162); // Thrawn
            map.put(174, 181); // Heavy Fire
            map.put(119, null); // None
            map.put(127, null); // Salacious B. Crumb
            map.put(156, null); // J4X-7

            map.put(175, 144); // Ahsoka Tano (IACP)
            map.put(176, 9); // BT-1 (IACP)
            map.put(177, 30); // Diala Passil (IACP)
            map.put(178, 34); // Echo Base Trooper [Elite] (IACP)
            map.put(179, 43); // Gamorrean Guard [Elite] (IACP)
            map.put(180, 178); // Kanan Jarrus (IACP)
            map.put(181, 72); // Kayn Somos (IACP)
            map.put(182, 151); // Ko-Tun Feralo (IACP)
            map.put(183, 76); // Leia Organa (IACP)
            map.put(184, 78); // Luke Skywalker (Hero of the Rebellion) (IACP)
            map.put(185, 79); // Luke Skywalker (Jedi Knight) (IACP)
            map.put(186, 152); // Maul (IACP)
            map.put(187, 92); // Probe Droid [Elite] (IACP)
            map.put(188, 97); // Rancor [Elite] (IACP)
            map.put(189, 102); // Rebel Trooper [Elite] (IACP)
            map.put(190, 159); // Sentry Droid [Elite] (IACP)
            map.put(191, 110); // Shyla Varad (IACP)
            map.put(192, 115); // Stormtrooper [Elite] (IACP)
            map.put(193, 121); // The Grand Inquisitor (IACP)
            map.put(194, 139); // Wing Guard [Elite] (IACP)
            map.put(195, 141); // Wookiee Warrior [Elite] (IACP)
            map.put(196, 2); // Advanced Com Systems (IACP)
            map.put(197, 1); // AT-ST [Elite] (IACP)
            map.put(198, 15); // Boba Fett (IACP)
            //map.put(200, 186); -- REMOVED -- // Cohesive Fireteam (IACP)
            map.put(201, 37); // Fenn Signis (IACP)
            map.put(202, 41); // Gaarkhan (IACP)
            map.put(203, 45); // General Weiss (IACP)
            map.put(204, 185); // Imperial Retrofitting (IACP)
            map.put(205, 71); // Jyn Odan (IACP)
            map.put(207, 105); // Royal Guard Champion (IACP)
            map.put(208, 187); // Suppressive Fire (IACP)
            map.put(209, 190); // 4-LOM (IACP)
            map.put(210, 13); // Biv Bodhrik (IACP)
            map.put(211, 27); // Dengar (IACP)
            map.put(212, 191); // Zuckuss (IACP)
            //map.put(213, 40); -- REMOVED -- // Fury of Kashyyyk (IACP)
            map.put(214, 86); // Obi-Wan Kenobi (IACP)
            map.put(215, 104); // Royal Guard [Elite] (IACP)
            map.put(216, 108); // Saska Teft (IACP)
            map.put(217, 107); // SC2-M Repulsor Tank [Elite] (IACP)
            map.put(218, 189); // Scout Trooper (IACP)
            map.put(219, 188); // Yoda (IACP)
            map.put(220, 126); // Tusken Raider [Elite] (IACP)
            map.put(221, 60); // ISB Infiltrator [Elite] (IACP)
            map.put(222, 4); // Agent Blaise (IACP)
            map.put(223, 198); // AT-RT [Elite] (IACP)
            map.put(224, 16); // Bossk (IACP)
            map.put(225, 201); // Jyn Erso (IACP)
            map.put(226, 200); // Director Krennic (IACP)
            map.put(227, 192); // IG-11 (IACP)
            map.put(228, 193); // The Mandalorian (IACP)
            map.put(229, null); // The Child (IACP)
            map.put(230, 195); // Clan of Two (IACP)
            map.put(231, 32); // E-Web Engineer [Elite] (IACP)
            map.put(232, 44); // General Sorin (IACP)
            map.put(233, 73); // Lando Calrissian (IACP)
            map.put(234, 77); // Loku Kanoloa (IACP)
            map.put(235, 196); // Shoretrooper [Elite] (IACP)
            map.put(236, 197); // Mortar Trooper [Elite] (IACP)
            map.put(237, 194); // Overwatch (IACP)
            map.put(238, 203); // Rebel Pathfinder [Elite] (IACP)
            map.put(239, 100); // Rebel Saboteur [Elite] (IACP)
            map.put(240, 202); // The Darksaber (IACP)
            map.put(241, 123); // Trandoshan Hunter [Elite] (IACP)
            map.put(242, 132); // Verena Talos (IACP)
            map.put(243, 204); // Cassian Andor (IACP)
            map.put(244, 205); // K-2SO (IACP)
        }

        // COMMAND CARDS: TTA => IAB
        {
            map = FromTTA.get(CardType.COMMAND);
            map.put(0, null); // None
            map.put(1, 75); // Heart of Freedom
            map.put(2, 130); // Self-Defense
            map.put(4, 50); // Espionage Mastery
            map.put(5, 90); // Intelligence Leak
            map.put(6, 138); // Smuggled Supplies
            map.put(7, 139); // Smugglers Tricks
            map.put(8, 32); // Crush
            map.put(9, 92); // Jundland Terror
            map.put(10, 111); // Opportunistic
            map.put(11, 117); // Parting Blow
            map.put(12, 19); // Capture the Weary
            map.put(13, 97); // Mandalorian Tactics
            map.put(14, 43); // Disorient
            map.put(15, 71); // Grisly Contest
            map.put(16, 157); // Trandoshan Terror
            map.put(17, 1); // Adrenaline
            map.put(18, 38); // Debts Repaid
            map.put(19, 81); // Hold Ground
            map.put(20, 128); // Roar
            map.put(21, 163); // Wookiee Rage
            map.put(22, 16); // Burst Fire
            map.put(23, 21); // Celebration
            map.put(24, 24); // Close the Gap
            map.put(25, 29); // Covering Fire
            map.put(26, 36); // Deadeye
            map.put(27, 39); // Deflection
            map.put(28, 47); // Element of Surprise
            map.put(29, 54); // Expose Weakness
            map.put(30, 58); // Ferocity
            map.put(31, 60); // Fleet Footed
            map.put(32, 64); // Force Lightning
            map.put(33, 68); // Furious Charge
            map.put(34, 72); // Guardian Stance
            map.put(35, 83); // Hunter Protocol
            map.put(36, 93); // Knowledge and Defense
            map.put(37, 95); // Lord of the Sith
            map.put(38, 96); // Lure of the Dark Side
            map.put(39, 98); // Marksman
            map.put(40, 100); // Maximum Firepower
            map.put(41, 101); // Meditation
            map.put(42, 189); // One in a Million
            map.put(43, 190); // Planning
            map.put(44, 191); // Price on Their Heads
            map.put(45, 192); // Pummel
            map.put(46, 193); // Rally
            map.put(47, 194); // Recovery
            map.put(48, 195); // Regroup
            map.put(49, 197); // Shadow Ops
            map.put(50, 198); // Sit Tight
            map.put(51, 199); // Son of Skywalker
            map.put(52, 200); // Take Cover
            map.put(53, 201); // Take Initiative
            map.put(54, 202); // Take it Down
            map.put(55, 203); // Telekinetic Throw
            map.put(56, 204); // To the Limit
            map.put(57, 205); // Urgency
            map.put(58, 14); // Brace Yourself
            map.put(59, 34); // Dangerous Bargains
            map.put(60, 118); // Payback
            map.put(61, 3); // Against the Odds
            map.put(62, 46); // Efficient Travel
            map.put(63, 80); // Hit and Run
            map.put(64, 59); // Field Tactician
            map.put(65, 67); // Fuel Upgrade
            map.put(66, 112); // Optimal Bombardment
            map.put(67, 49); // Endless Reserves
            map.put(68, 53); // Explosive Weaponry
            map.put(69, 76); // Heavy Armor
            map.put(70, 107); // New Orders
            map.put(71, 115); // Overrun
            map.put(72, 88); // In the Shadows
            map.put(73, 121); // Primary Target
            map.put(74, 147); // Stroke of Brilliance
            map.put(75, 42); // Disable
            map.put(76, 86); // I Make My Own Luck
            map.put(77, 89); // Inspiring Speech
            map.put(78, 127); // Reposition
            map.put(79, 137); // Slippery Target
            map.put(80, 108); // Of No Importance
            map.put(81, 10); // Blaze of Glory
            map.put(82, 41); // Dirty Trick
            map.put(83, 102); // Merciless
            map.put(84, 114); // Overdrive
            map.put(85, 126); // Repair
            map.put(86, 26); // Comm Disruption
            map.put(87, 35); // Data Theft
            map.put(88, 145); // Strategic Shift
            map.put(89, 123); // Rally the Troops
            map.put(90, 124); // Rank and File
            map.put(91, 140); // Squad Swarm
            map.put(92, 8); // Black Market Prices
            map.put(93, 23); // Cheat to Win
            map.put(94, 141); // Stall for Time
            map.put(95, 7); // Behind Enemy Lines
            map.put(96, 84); // I Can Feel It
            map.put(97, 153); // There is Another
            map.put(98, 0); // A Powerful Influence
            map.put(99, 66); // Force Surge
            map.put(100, 85); // I Must Go Alone
            map.put(101, 40); // Devotion
            map.put(102, 51); // Etiquette and Protocol
            map.put(103, 73); // Hard to Hit
            map.put(104, 135); // Single Purpose
            map.put(105, 151); // Terminal Network
            map.put(106, 22); // Change of Plans
            map.put(107, 25); // Collect Intel
            map.put(108, 48); // Emergency Aid
            map.put(109, 79); // Hide in Plain Sight
            map.put(110, 94); // Lock On
            map.put(111, 62); // Focus
            map.put(112, 70); // Grenadier
            map.put(113, 125); // Reinforcements
            map.put(114, 146); // Strength in Numbers
            map.put(115, 27); // Coordinated Attack
            map.put(116, 74); // Harsh Environment
            map.put(117, 99); // Master Operative
            map.put(118, 103); // Miracle Worker
            map.put(119, 106); // Negation
            map.put(120, 113); // Overcharged Weapons
            map.put(121, 132); // Set a Trap
            map.put(122, 136); // Size Advantage
            map.put(123, 148); // Survival Instincts
            map.put(124, 13); // Bodyguard
            map.put(125, 28); // Counter Attack
            map.put(126, 30); // Cripple
            map.put(127, 61); // Flurry of Blades
            map.put(128, 131); // Set for Stun
            map.put(129, 143); // Stealth Tactics
            map.put(130, 18); // Camouflage
            map.put(131, 56); // Fatal Deception
            map.put(132, 63); // Force Illusion
            map.put(133, 160); // Vanish
            map.put(134, 37); // Deadly Precision
            map.put(135, 65); // Force Rush
            map.put(136, 82); // Hunt Them Down
            map.put(137, 31); // Cruel Strike
            map.put(138, 78); // Hidden Trap
            map.put(139, 142); // Stay Down
            map.put(140, 87); // Improvised Weapons
            map.put(141, 162); // Wild Fury
            map.put(142, 134); // Shoot the Messenger
            map.put(143, 4); // Assassinate
            map.put(144, 12); // Blood Feud
            map.put(145, 91); // Jump Jets
            map.put(146, 155); // Tough Luck
            map.put(147, 161); // Wild Attack
            map.put(148, 17); // Call the Vanguard
            map.put(149, 77); // Heightened Reflexes
            map.put(150, 104); // Mitigate
            map.put(151, 6); // Battlefield Awareness
            map.put(152, 20); // Cavalry Charge
            map.put(153, 57); // Feral Swipes
            map.put(154, 11); // Blitz
            map.put(155, 116); // Parry
            map.put(156, 120); // Positioning Advantage
            map.put(157, 110); // On the Lam
            map.put(158, 154); // Tools for the Job
            map.put(159, 144); // Stimulants
            map.put(160, 119); // Pickpocket
            map.put(161, 156); // Toxic Dart
            map.put(162, 69); // Glory of the Kill
            map.put(163, 129); // Run for Cover
            map.put(164, 44); // Draw!
            map.put(165, 55); // Extra Protection
            map.put(166, 9); // Bladestorm
            map.put(167, 196); // Sarlacc Sweep
            map.put(168, 122); // Provoke
            map.put(169, 133); // Shared Experience
            map.put(170, 5); // Ballistics Matrix
            map.put(171, 158); // Triangulate
            map.put(172, 109); // On a Mission
            map.put(173, 133); // Shared Experience
            map.put(174, 45); // Eerie Visage
            map.put(175, 159); // Utinni!
            map.put(176, 105); // Navigation Upgrade
            map.put(177, 2); // Advance Warning
            map.put(178, 150); // Targeting Network
            map.put(179, 52); // Evacuate
            map.put(180, 187); // Looking for a Fight
            map.put(181, 33); // Cut Lines
            map.put(182, 15); // Brace for Impact
            map.put(183, 149); // Take Position
            map.put(184, 166); // Battle Scars
            map.put(185, 167); // Collateral Damage
            map.put(186, 170); // Field Supply
            map.put(187, 172); // Heavy Ordnance
            map.put(188, 173); // Ready Weapons
            map.put(189, 152); // Terminal Protocol
            map.put(190, 149); // Take Position
            map.put(191, 174); // Balancing Force
            map.put(192, 175); // Fool Me Once
            map.put(193, 176); // Force Jump
            map.put(194, 177); // Force Push
            map.put(195, 178); // Right Back At Ya!
            map.put(196, 184); // Chaotic Force
            map.put(197, 185); // Deathblow
            map.put(198, 186); // Face to Face
            map.put(199, 187); // Looking for a Fight
            map.put(200, 188); // Wreak Vengeance
            map.put(201, 179); // Corrupting Force
            map.put(202, 180); // Dark Energy
            map.put(203, 182); // Prepared for Battle
            map.put(204, 183); // Unlimited Power
            map.put(205, 164); // Arcing Shot
            map.put(206, 165); // Armed Escort
            map.put(207, 168); // Concentrated Fire
            map.put(208, 169); // Droid Mastery
            map.put(209, 171); // Forward March
            map.put(210, 181); // Officer's Training
            map.put(211, 224); // Spinning Kick
            map.put(212, 226); // Wild Fire
            map.put(213, 223); // Signal Jammer
            map.put(214, 222); // Second Chance
            map.put(215, 220); // Protect the Old Ways
            map.put(216, 214); // Learn by Example
            map.put(217, 211); // Hour of Need
            map.put(218, 207); // Dying Lunge
            map.put(219, 213); // Karabast
            map.put(220, 221); // Rebel Graffiti
            map.put(221, 225); // Veteran Instincts
            map.put(222, 210); // Hostile Negotiation
            map.put(223, 215); // Lets Make a Deal
            map.put(224, 217); // Out of Time
            map.put(225, 227); // Worth Every Credit
            map.put(226, 216); // Marked Territory
            map.put(227, 218); // Pack Alpha
            map.put(228, 212); // Induce Rage
            map.put(229, 219); // Price of Glory
            map.put(230, 209); // Foresee
            map.put(231, 206); // Combat Resupply
            map.put(232, 208); // Escalating Hostility

            map.put(233, 4); // Assassinate (IACP)
            map.put(235, 110); // On the Lam (IACP)
            map.put(236, 39); // Deflection (IACP)
            map.put(238, 97); // Mandalorian Tactics (IACP)
            map.put(239, 93); // Knowledge and Defense (IACP)
            map.put(240, 228); // Close and Personal (IACP)
            map.put(241, 229); // Findsman Meditation (IACP)
            map.put(242, 230); // Get Behind Me! (IACP)
            map.put(243, 116); // Parry (IACP)
            map.put(244, 231); // Preservation Protocol (IACP)
            map.put(245, 232); // Soften the Blow (IACP) ==> Iron Will (IACP)
            map.put(246, 233); // There Is No Try (IACP)
            map.put(247, 232); // Iron Will (IACP)
            map.put(248, 236); // Built on Hope (IACP)
            map.put(249, 237); // Deploy the Garrison! (IACP)
            map.put(250, 234); // Guild Programming (IACP)
            map.put(251, 235); // Whistling Birds (IACP)
            map.put(252, 238); // Blend In (IACP)
        }
    }

    @Override
    public CardSystem getCardSystem(List<Integer> deploymentCardIds, List<Integer> commandCardIds) {
        for (int id : deploymentCardIds) {
            if (id >= TTA_FIRST_IACP_DEPLOYMENT_CARD) {
                return CardSystem.IACP;
            }
        }
        for (int id : commandCardIds) {
            if (id >= TTA_FIRST_IACP_COMMAND_CARD) {
                return CardSystem.IACP;
            }
        }
        return CardSystem.FFG;
    }

    @Override
    public Card toCard(CardSystem cardSystem, CardType cardType, Integer id) {
        Card card = null;
        Integer cardId = FromTTA.get(cardType).get(id);
        if (cardId != null) {
            card = cardType.getCard(cardSystem, cardId);
        }
        return card;
    }

    @Override
    public Integer fromCard(Card card) {
        return ToTTA.get(card.getCardSystem()).get(card.getCardType()).get(card.getId());
    }

}

package tech.pursuiters.techpursuiters.uitsocieties.about_fragment;

import tech.pursuiters.techpursuiters.uitsocieties.R;

import static android.R.attr.level;
import static tech.pursuiters.techpursuiters.uitsocieties.R.drawable.shankhnaad;

/**
 * Created by Shubhi on 20-Dec-17.
 */

public class AboutDataModel {

    private int imageResourceId;
    private String number;
    private String name;
    private String about;
    private String objective;
    private String email;
    private String fblink;
    private String website;

    private AboutDataModel(String name, String objective, String about, String number, String email, String fblink, String website, int imageResourceId) {
        this.name = name;
        this.about = about;
        this.objective = objective;
        this.number = number;
        this.email = email;
        this.fblink = fblink;
        this.website = website;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getObjective() {
        return objective;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getFblink() {
        return fblink;
    }

    public String getWebsite() {
        return website;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public static final AboutDataModel[] details = {

            new AboutDataModel("AAVAHAN","","ABOUT: \n\nA three day long cultural fest open for all the colleges of Madhya Pradesh.\nDates- 2nd-4th May'17\nVenue- UIT-RGPV campus","08989699289","aavahan.rgpv@gmail.com","https://www.facebook.com/aavahanrgpv/","http://www.aavahan.in",R.drawable.aavahan),

            new AboutDataModel("ACM: ASSOCIATION FOR COMPUTING MACHINERY RGPV", "", "ABOUT\n\nThe Association for Computing Machinery is an international learned society for computing. It was founded in 1947 and is the world's largest scientific and educational computing society. It is a not-for-profit professional membership group.", "", "", "https://www.facebook.com/acmrgpv/", "http://acmrgtu.acm.org\nhttp://acmwrgpv.acm.org", R.drawable.acm),

            new AboutDataModel("MOZILLA CLUB COHERENT", "MISSION\n\nBelieve in Openness, Innovation and Creativity", "We are the open source enthusiasts from Bhopal contributing in various Mozilla projects to make internet better and accessible to all.\n Awarded by Club of the month in November 2015 for the month Oct 2015", "", "", "https://www.facebook.com/Coherentatuit/", "\n" +
                    "https://wiki.mozilla.org/Coherent_@_UIT\n", R.drawable.coherent),

            /*new AboutDataModel("DISHANJALI","MISSION\n\nINDIA WILL BE LEADER IN EDUCATION AND RESEARCH.\n\nNAV SHIKSHIT BHARAT MISSION","ABOUT\n\nFounded in July 2013\n\nEDUCATED INDIA\nLEADING INDIA\nDEVELOPED INDIA","09977555385","dishanjali.edu@gmail.com","https://www.facebook.com/NSBMDishanjali/","http://dishanjali.wix.com/nsbm",R.drawable.dishanjali),*/

            new AboutDataModel("ENTREPRENEURSHIP CELL RGPV", "", "ABOUT\n\nFounded on May 9, 2016\nThe Entrepreneurship Cell of RGPV is a Non - Profit Organization.", "088279 70446", "contact@ecellrgpv.in", "https://www.facebook.com/ECellRGPV/", "\n" +
                    "http://www.ecellrgpv.in", R.drawable.ecell),

            new AboutDataModel("THE GREEN ARMY", "MISSION\n\n\"Creating endless ripples every day\"", "ABOUT\n\nThe Green Army was founded in 2011 by Yuvraj Puri, Tayyab Zafar,Umar M Khan and Varsha Dange.\nTHE GREEN ARMY has been amplified to ' The Dream Box Foundation ' - a social reforming society, prioritizing environment, health and education.", "096857 41401",
                    "greenarmyrgpv@gmail.com", "https://www.facebook.com/TheGreenArmyUIT/",
                    "http://thedreambox.in", R.drawable.green_army),

            new AboutDataModel("HackerEarth", "", "ABOUT\n\n HackerEarth UIT RGPV is a Programming Hub to promote competitive programming in our University. \n" +
                    "So, Compete with our friends & college mates and portray your Programming Skills.\n" +
                    "Keep Coding!!\n", "",
                    "piyushpanjwani12@gmail.com", "https://www.facebook.com/HackerEarthUIT/","", R.drawable.hacker_earth),

            new AboutDataModel("INSYNC", "", "ABOUT\n\nInSync Cultural Society is a vibrant dynamic student body which serves as a platform for youth to show their Cultural skills. The foremost aims of the society are to promote the richness and diversity of the Indian subcontinents heritage, art and culture and to foster goodwill and harmony between different communities and cultures.\n" +
                    "\n" +
                    "InSync comprises of the five sections which take care of the varied fields of Cultural events :\n" +
                    "1. Literary Section \n\n" +
                    "The section of ingenious Speaker's & Writer's . It has been conducting various brainstorming & witty events. \n" +
                    "2. Dance Section \n\n" +
                    "The section consisting of vibrant Dancers. It aims at encouraging, practicing and performing various genres of music (classical, western, fusion etc.). \n" +
                    "3. Music Section \n\n" +
                    "The section of the melodious Instrumentalists & Vocalists. From Soft Music to the Hard Rock, it presents all kinds of musical events.\n" +
                    "4. Drama Section \n\n" +
                    "The section of real Actors. It has been staging plays of unbeatable standards & has also been the most distinctive among all. \n" +
                    "5. Creative Section \n\n" +
                    "The section of genius Minds. It's been leading the inventive & esthetic events to pull out the creativity in each individual. \n" +
                    "\n" +
                    "InSync conveys to all... \n\n" +
                    "\"Being an Artist\" leads to Origin;\n" +
                    "\"Joining us\" leads to Progress ; \n" +
                    "\"Working together\" leads to Success.....!!!", "", "", "https://www.facebook.com/insync.uitrgpv/", "\n" +
                    "http://www.insyncatuit.cf", R.drawable.insync),

            new AboutDataModel("ISpeakAalay", "", "ABOUT\n\nISpeakAalay - The weekly magazine from the InSync Literary Club of UIT-RGTU.\n The magazine is a platform for all the students to explore the activities , share their brilliance , realize their creativity and get amazed with the amusing content to discover the incredible ! \n" +
                    "The magazine focuses on vivid and vibrant facets of the college life. It's an initiative to bring out the flair of each student by providing the platform to Speak.It also provides the coverage of various Cultural and Technical events of the colleges. \n", "", "", "https://www.facebook.com/ISpeakAalay/", "\n" +
                    "https://ispeakaalaymagazine.wordpress.com\n", R.drawable.ispeakaalay),

            new AboutDataModel("MAHASANGRAM", "", "ABOUT\n\nSports Event", "", "", "https://www.facebook.com/mahasangram2k17/", "\n" +
                    "http://www.mahasangram2k17.blogspot.com\n", R.drawable.mahasangram),

            new AboutDataModel("PHOENIX", "MISSION\n\nBridge the gap for students, between the inspiring ideas and their implementation.", "ABOUT\n\nPhoenix is the oldest official society of UIT RGPV which strives towards the goal of development of students with its highly dedicated committee.\n", "", "contact@phoenixatuit.com", "https://www.facebook.com/convolution2018/", "http://phoenixatuit.com", R.drawable.phoenix),

            new AboutDataModel("शंखनाद", "", "ABOUT\n\nराजीव गाँधी प्रौद्योगिकी विश्वविद्यालय का प्रथम हिंदी महोत्सव |", "", "shankhnaad.rgpv@gmail.com", "https://www.facebook.com/ShankhnaadRGPV/", "", R.drawable.shankhnaad),

            new AboutDataModel("SRIJAN", "MISSION \n\nBuild Business/Management and Entrepreneurship skills in Student but also providing students a way to learn things and have Fun.","ABOUT \n\nFounded on February 15, 2016.\nWe are the Management Society of UIT RGPV and we're here to create opportunities to the students for bringing the best version of them through our events.", "09425108370", "decrypt.uit.rgpv@gmail.com", "https://www.facebook.com/Srijan.uit/", "http://srijan.host22.com", R.drawable.srijan ),

            new AboutDataModel("SUNDARBAN", "MISSION\n\nNature conservation and promoting sustainable development", "ABOUT\n\nEnvironmental Society\nStarted on November 28, 2016\nOur society is about protecting our mother nature. We believe in sustainable development. We encourage people working for conservation of environment and promote ecological lifestyle.", "", "sundarban1827@gmail.com", "https://www.facebook.com/sundarban1827/", "", R.drawable.sundarban),

            new AboutDataModel("TECHNOPHILIC", "", "ABOUT\n\n" + "Technophilic is a club of civil engineering students of Rajeev Gandhi Technological University which is keenly interested to spread the ideas of technology.\n", "", "", "https://www.facebook.com/technophilic.rgpv/", "http://www.technophilic.in", R.drawable.technophilic),

            new AboutDataModel("TEDx RGPV", "", "ABOUT\n\n TEDxRGPV aims to provide a TED-like experience to the RGPV community by bringing out the best series of talks. The TEDx Program is designed to help communities, organizations, and individuals to spark conversation and connection through local TED-like experiences.\n\nAt TEDx events, a screening of TED Talks videos — or a combination of live presenters and TED Talks videos — sparks deep conversation and connections at the local level.", "09753815821", "", "https://www.facebook.com/tedxrgpv/", "", R.drawable.tedxrgpv)
    };
}

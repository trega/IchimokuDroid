package com.projects.trega.ichimokudroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.DataDownloader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.projects.trega.ichimokudroid.CommonInterface.STOCK_DATA_FILE_NAME;
import static com.projects.trega.ichimokudroid.CommonInterface.STOCK_DATA_SYMBOL;

public class MainActivity extends AppCompatActivity {
    static String TAG ="MAIN_ACTIVITY";
    DataDownloader itsDataDownloader;
    DataCenter itsDataCenter;
    File itsStockFile;

    private String symbolName;
    private String dataFileName;
    private String dataFilePath;
    private DownloadParametersBoundle downloadParametersBundle;
    private final String[] symbols ={
            "01C", "06N", "08N", "11B", "1AT", "2CP", "2IT", "4FM", "4MB", "4X4", "5AH", "71M",
            "AAL", "AAT", "ABA", "ABC", "ABE", "ABK", "ABM", "ABS", "ACA", "ACG", "ACP", "ACR", "ACS", "ACT",
"ADF", "ADM", "ADV", "AED", "AER", "AFC", "AFH", "AGL", "AGM", "AGO", "AGP", "AGR", "AGT", "AHL",
"AIN", "AIT", "ALC", "ALD", "ALI", "ALK", "ALL", "ALM", "ALR", "ALS", "ALU", "AMB", "AMC", "AMG",
"AML", "ANM", "AOL", "APA", "APE", "API", "APL", "APN", "APR", "APS", "APT", "AQA", "AQU", "ARA",
"ARC", "ARE", "ARH", "ARI", "ARR", "ART", "ARX", "ASA", "ASB", "ASE", "ASG", "ASM", "ASP", "ASR",
"ASS", "AST", "ATA", "ATC", "ATD", "ATG", "ATL", "ATM", "ATO", "ATP", "ATR", "ATS", "ATT", "AUX",
"AVC", "AVE", "AWB", "AWG", "AWM", "AZC", 
"B2B", "B3S", "BAC", "BAL", "BBA", "BBD", "BCI", "BCM", "BDF", "BDG", "BDL", "BDX", "BDZ", "BEP",
"BER", "BFC", "BFT", "BGD", "BGE", "BGZ", "BHW", "BHX", "BIK", "BIM", "BIO", "BIP", "BKM", "BLO",
"BLR", "BLT", "BLU", "BMC", "BML", "BMP", "BMR", "BMX", "BOA", "BOS", "BOW", "BPC", "BPN", "BPX",
"BRA", "BRG", "BRI", "BRO", "BRS", "BRU", "BSA", "BSC", "BST", "BTG", "BTM", "BTX", "BUM", "BVT",
"BWO", "BZW",
"CAR", "CCC", "CCE", "CCS", "CCT", "CDL", "CDR", "CEZ", "CFB", "CFI", "CFL", "CHS", "CIE", "CIG",
"CLC", "CLD", "CLE", "CLN", "CLS", "CMC", "CMP", "CMR", "CNG", "CNT", "COG", "COL", "COM", "COR",
"CPA", "CPD", "CPG", "CPL", "CPS", "CRM", "CRP", "CRS", "CSR", "CSY", "CTC", "CTE", "CTF", "CTG",
"CTS", "CWP", "CZK", "CZT",
"D24", "DAM", "DBC", "DCD", "DCR", "DEK", "DEL", "DEV", "DFG", "DGA", "DGL", "DIN", "DKR", "DLK",
"DNS", "DOA", "DOM", "DPL", "DRE", "DRP", "DYW",
"EAH", "EAT", "EBC", "EBX", "EC2", "ECA", "ECH", "ECK", "ECL", "ECO", "ECR", "EDI", "EDN", "EEX",
"EFE", "EFK", "EFX", "EGB", "EGH", "EGS", "EHG", "EKG", "EKP", "EKS", "ELB", "ELM", "ELT", "ELZ",
"EMC", "EMM", "EMP", "EMT", "EMU", "ENA", "ENE", "ENG", "ENI", "ENP", "ENT", "EON", "ERB", "ERC",
"ERG", "ERN", "ERS", "ESC", "ESK", "ESS", "EST", "ETL", "ETR", "ETX", "EUC", "EUR", "EVE", "EXA",
"EXC", "EXL", "EZO",
"F51", "FAM", "FAV", "FCL", "FEE", "FEG", "FER", "FFI", "FGT", "FHD", "FIN", "FKD", "FLD", "FMF",
"FMG", "FON", "FOR", "FOT", "FPO", "FRO", "FSG", "FTE", "FTN", "FUT",
"GAL", "GCI", "GCN", "GEM", "GEN", "GEU", "GJA", "GKI", "GKP", "GKS", "GLC", "GLG", "GNB", "GNG",
"GNR", "GOB", "GOL", "GPP", "GPW", "GRC", "GRE", "GRI", "GRL", "GRM", "GRN", "GRP", "GRT", "GRV",
"GTC", "GTF", "GTK", "GTN", "GTP", "GTR", "GTS", "GTY", "GWR",
"HDR", "HEF", "HEL", "HFT", "HLD", "HMI", "HOR", "HOT", "HPS", "HRC", "HRP", "HRS", "HRT", "HTM",
"HTN", "HUB", "HWE", "HYP",
"I2D", "I3D", "IAG", "IAI", "IAP", "IBC", "IBS", "ICA", "ICD", "ICM", "ICP", "IDA", "IDE", "IDG",
"IDH", "IDM", "IDT", "IF4", "IFA", "IFC", "IFM", "IFR", "IFS", "IGN", "IGT", "IIA", "IMC", "IMG",
"IMP", "IMS", "INB", "INC", "IND", "INF", "ING", "INK", "INL", "INP", "INS", "INT", "INV", "INW",
"IOD", "IPE", "IPF", "IPL", "IPO", "IPT", "IPX", "IQP", "IRL", "ISA", "ISG", "IST", "ITB", "ITG",
"ITL", "IUS", "IVC", "IVE", "IZB", "IZO", "IZS", "JAN", "JHM", "JJB", "JJO", "JRH", "JSW", "JWA",
"JWC", "JWW",
"K2I", "KAN", "KBD", "KBJ", "KCH", "KCI", "KDM", "KER", "KFM", "KGH", "KGL", "KGN", "KIN", "KKH",
"KLN", "KME", "KMP", "KOF", "KOM", "KOR", "KPC", "KPD", "KPI", "KPL", "KPX", "KRC", "KRI", "KRK",
"KRS", "KRU", "KRUA", "KSG", "KST", "KSW", "KTY", "KVT", "KZS",
"LAB", "LAN", "LBD", "LBT", "LBW", "LCC", "LEN", "LET", "LGT", "LGZ", "LKD", "LKS", "LPP", "LPS",
"LRK", "LRQ", "LSH", "LSI", "LSR", "LTG", "LTS", "LTX", "LUG", "LUX", "LVC", "LWB", "LYD", "LZM",
"M4B", "MAB", "MAD", "MAK", "MAR", "MAX", "MBF", "MBK", "MBP", "MBR", "MBW", "MCE", "MCI", "MCL",
"MCP", "MCR", "MDA", "MDG", "MDI", "MDN", "MDP", "MDV", "MEG", "MEI", "MER", "MEX", "MFD", "MFO",
"MGA", "MGC", "MGF", "MGM", "MGS", "MGT", "MID", "MIL", "MIR", "MLB", "MLG", "MLK", "MLP", "MMA",
"MMC", "MMD", "MMF", "MMS", "MNC", "MND", "MNI", "MNS", "MNX", "MOB", "MOD", "MOE", "MOJ", "MOL",
"MOM", "MON", "MPH", "MPY", "MRA", "MRB", "MRC", "MRH", "MRK", "MRS", "MSP", "MSW", "MSZ", "MTL",
"MTR", "MVP", "MWT", "MXP", "MZA", "MZN",
"NAN", "NCT", "NEM", "NET", "NEU", "NFP", "NIN", "NOV", "NTS", "NTT", "NTW", "NVA", "NVT", "NVV",
"NWA", "NWG", "NWR",
"OBL", "ODL", "OEG", "OEX", "OIL", "ONC", "OPE", "OPF", "OPFA", "OPG", "OPL", "OPM", "OPN", "OPT",
"ORB", "ORG", "ORL", "ORN", "ORP", "OTM", "OTS", "OUT", "OVO",
"P24", "PAT", "PBB", "PBF", "PBG", "PBX", "PCE", "PCG", "PCI", "PCM", "PCR", "PCV", "PCX", "PDZ",
"PEL", "PEM", "PEN", "PEO", "PEP", "PEX", "PFD", "PFL", "PFM", "PFR", "PGD", "PGE", "PGM", "PGN",
"PGO", "PGP", "PGS", "PHN", "PHR", "PIG", "PIK", "PIL", "PIS", "PIW", "PJP", "PKN", "PKO", "PKP",
"PLA", "PLE", "PLI", "PLM", "PLT", "PLW", "PLX", "PLZ", "PMA", "PME", "PMG", "PMP", "PMT", "PND",
"POM", "POZ", "PPS", "PRC", "PRD", "PRF", "PRI", "PRK", "PRL", "PRM", "PRN", "PRO", "PRS", "PRT",
"PRV", "PSM", "PST", "PSW", "PTE", "PTN", "PTW", "PUE", "PWM", "PWX", "PXM", "PYL", "PYLA", "PZU",
"QMK", "QNT", "QRK", "QRS", "QRT", "QUB", "RAF", "RBC", "RBS", "RBW", "RCA", "RCW", "RDG", "RDL",
"RDN", "REG", "REM", "RES", "REV", "RFK", "RHD", "RLP", "RMK", "RNK", "ROB", "RON", "ROT", "ROV",
"RPC", "RSY", "RUN", "RWD", "RWL",
"S4E", "SAE", "SAN", "SAP", "SAR", "SBE", "SCG", "SCO", "SCS", "SEA", "SEK", "SEL", "SEN", "SES",
"SET", "SEV", "SFD", "SFG", "SFI", "SFK", "SFN", "SFS", "SGN", "SGR", "SHD", "SHG", "SKA", "SKH",
"SKL", "SKN", "SKT", "SLV", "SME", "SMK", "SMS", "SNK", "SNS", "SNT", "SNW", "SNX", "SOL", "SON",
"SP1", "SPH", "SPK", "SSK", "SSO", "STD", "STE", "STF", "STK", "STL", "STP", "STT", "STX", "SUL",
"SUN", "SUW", "SWD", "SWG", "SWP", "SYM", "SZR",
"T2P", "TAR", "TBL", "TDX", "TFO", "TIG", "TIM", "TLG", "TLO", "TLS", "TLV", "TLX", "TME", "TMP",
"TMR", "TNX", "TOA", "TOR", "TOS", "TOW", "TPE", "TPM", "TPR", "TRC", "TRI", "TRK", "TRN", "TRR",
"TSG", "TXF", "TXM", "TXN", "TYP",
"U2K", "UCG", "UFC", "ULM", "UNI", "UNT", "UNW", "URS", "UTD",
"VCP", "VDS", "VED", "VEL", "VER", "VGO", "VIA", "VIN", "VIV", "VKT", "VOI", "VOT", "VOX", "VRB",
"VST", "VTE", "VTG", "VTI", "VTL", "VVD", "VVR",
"WAS", "WAX", "WBY", "WDB", "WDX", "WES", "WHH", "WIK", "WIS", "WLB", "WLT", "WOD", "WOJ", "WPL",
"WRE", "WRL", "WSE", "WST", "WTN", "WWL", "WXF",
"XPL", "XSM", "XTB",
"YAN", "YOL",
"ZAP", "ZEP", "ZMT", "ZRE", "ZST", "ZUE", "ZUK", "ZWC"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itsDataCenter = new DataCenter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeSymbolEditBox();


        initializeFloatingButtons();
    }

    private void initializeFloatingButtons() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractDownloadParameters();

                File tmpFile = new File(dataFilePath);
                if(!tmpFile.exists()) {
                    itsDataCenter.acquireData(downloadParametersBundle);
                }
                else {
                    itsStockFile=tmpFile;
                    String message = "File already exists, not downloading again: " + itsStockFile.getAbsolutePath();
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                    Log.d(TAG, message);
                    dataReady(itsStockFile);
                }
            }
        });

        FloatingActionButton butCleanStorage = (FloatingActionButton) findViewById(R.id.butCleanStorage);
        butCleanStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogBoxStyle);
                builder.setMessage("Do you want to remove all files in:\n"+getString(R.string.storagePath ))
                        .setTitle("Clear data storage").setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File storageDir = new File (getString(R.string.storagePath));
                        for(File file: storageDir.listFiles())
                            if (!file.isDirectory())
                                file.delete();
                    }
                })                ;
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initializeSymbolEditBox() {
        AutoCompleteTextView symbolNameEt = (AutoCompleteTextView) findViewById(R.id.symbolValEt);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,symbols);
        symbolNameEt.setThreshold(0);
        symbolNameEt.setAdapter(adapter);
        symbolNameEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView)v).showDropDown();
                return true;
            }
        });
    }

    private void extractDownloadParameters() {
        AutoCompleteTextView symbolNameEt = (AutoCompleteTextView) findViewById(R.id.symbolValEt);
        symbolName = symbolNameEt.getText().toString();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = c.getTime();
        c.add(Calendar.YEAR, -2);
        Date yearBackTime = c.getTime();
        String formattedDateCurrent = df.format(currentTime);
        String[] currentDateArray = formattedDateCurrent.split("-");
        String formattedDatePast = df.format(yearBackTime);
        String[] pastDateArray = formattedDatePast.split("-");
        downloadParametersBundle = new DownloadParametersBoundle(
                symbolName, currentDateArray[0], currentDateArray[1], currentDateArray[2],
                pastDateArray[0], pastDateArray[1], pastDateArray[2]);
        dataFileName = symbolName + "_d.csv_"+downloadParametersBundle.getPastDateDownloadString()+
                "_"+downloadParametersBundle.getCurrentDateDownloadString()+".txt";
        dataFilePath = getString(R.string.storagePath) + dataFileName;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dataReady(File stockFile) {
        itsStockFile = stockFile;
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra(STOCK_DATA_FILE_NAME, stockFile.getAbsolutePath());
        intent.putExtra(STOCK_DATA_SYMBOL, symbolName);
        startActivity(intent);
    }
}

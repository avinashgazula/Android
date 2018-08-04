//getting time

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
String currentTime = df.format(new Date());
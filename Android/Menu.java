//Menu

//in activity_main create a "group" and add "item"s to the group
/*
	<group android:checkableBehavior="none">
        <item
            android:id="@+id/Red"
            android:orderInCategory="1"
            app:showAsAction="never"
            android:title="Red" />

     </group>


*/
 
public boolean onOptionsItemSelected(MenuItem item){
	
	int id = item.getItemId();
	
	if (id == R.id.*id of menu item*) {
           
		   //code
		   
            return true;
        }
	return super.onOptionsItemSelected(item);
}
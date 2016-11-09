 

function EnterField(){
  var newAge = document.getElementById("age").value;
  var newFirstName = document.getElementById("first_name").value;
  var newLastName = document.getElementById("last_name").value;
  var newphone = document.getElementById("phone").value;
  var newadress = document.getElementById("address").value;

  if(newAge.length < 1 || newFirstName.length < 1 || newLastName.length < 1){
    document.getElementById("confirmation").innerHTML = "All fields must be entered."
    return;
  }
	if(isNaN(newAge)){
    document.getElementById("confirmation").innerHTML = "Age must be a number.";
    return;
  }
	var params = {
        "first_name": newFirstName,
        "last_name": newLastName,
        "age": newAge,
        "address": newadress,
        "phone": newphone             
    };

    $.ajax({
      type: 'POST',
      url: 'enter_name',
      data: params,
      success: function (data) {
        document.getElementById("confirmation").innerHTML = 
        newFirstName + " " + newLastName + ", age: " + newAge + "<br>address: " + newadress+"<br>phone: " + newphone + "<br>has just been entered";
        document.getElementById("first_name").value = "";
        document.getElementById("last_name").value = "";
        document.getElementById("age").value = "";  
        document.getElementById("address").value = "";  
        document.getElementById("phone").value = "";           
      }
    });
}

function GenerateRandom(){
  $.ajax({
      type: 'POST',
      url: 'generate_random',
      success: function (data) {
        GetValues();        
      }
    });
}

function DeleteAll(){
  $.ajax({
      type: 'POST',
      url: 'delete_entries',
      success: function (data) {
        GetValues();        
      }
    });
}

function GetOrdered(){
  GetValues(document.getElementById("orderselection").value, document.getElementById("orderselectiondirection").value)
}

function GetValues(filter = null, direction = null){
  var params = {};
  if(filter !== null){            
    params.sort = filter,
    params.direction = direction
   }
    $.ajax({
      type: 'POST',
      url: 'get_table',
      data: params,
      success: function (response) {
        DrawTables(JSON.parse(response));            
      }
    });
}

function DrawTables(data){
  var str = "<table>";
  for(var i = 0; i < data.length; i++){
    str+="<tr>"
    str+="<td> " + data[i].firstName + "</td>";
    str+="<td> " + data[i].lastName + "</td>";
    str+="<td> " + data[i].age + "</td>";
    str+="</tr>"
  }
  str+="</table>";
  document.getElementById("myTable").innerHTML = str;
}

var filterCount = 1;
function AddFilter(){
  filterCount++;
  str = '<div id = "filter' + filterCount + '">'+
        '<select name="filterOf" id="filterOf">'+
        '<option value="firstName">first name</option>'+
        '<option value="lastName">last name</option>'+
        '<option value="age">age</option>'+
        '</select> '+

        '<select name="filterBy" id="filterBy">'+
        '<option value="filtercontains">contains</option>'+
        '<option value="filteris">is (==)</option>'+
        '<option value="filterstartswith">starts with</option>'+
        '<option value="filterendswith">ends with</option>'+
        '</select> '+

        '<input id="filterValue" type="text" name="filterValue" value=""><br>'+
        '</div>';
  $("#filters").append(str);
}

function SearchFilterd(){
  data = [];
  count = 0;
  $('#filters').find('div').each(function(){
    var innerDivId = $(this).attr('id');
    var str = "#"+innerDivId;
    data.push({
      "filterOf": $(str).find('#filterOf')[0].value,
      "filterBy": $(str).find('#filterBy')[0].value,
      "filterValue": $(str).find('#filterValue')[0].value
    });
    count++;
  });
  console.log(data);

  var params = {
    "filters": data,
    "filterCount": count
  }
  $.ajax({
    type: 'POST',
    url: 'get_table',
    data: params,
    success: function (response) {
      console.log(response);
      DrawTables(JSON.parse(response));            
    }
  });
}

function getJson(){
  params = {
    "stuff": "ishere"
  }
  $.ajax({
    type: 'POST',
    url: 'testfunction',
    data: params,
    success: function (response) {
      console.log(response);
    }
  });
}





























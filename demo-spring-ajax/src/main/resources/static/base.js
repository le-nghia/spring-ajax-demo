$(document).ready(function (){

    displayCustomer();
    console.log("Chay ham nay");
    renderBrandsTable();

    const $brandInfoForm = $('#brandInfoForm');
    const $brandInfoModal = $('#brandInfoModal');

    /* Search */
    // Search brand when clicking Search button
    $('#searchBrand').on('click', function() {
        searchBrandName();
    })

    // Search when press Enter
    $('.form-search').keypress((e) => {
        if (e.which === 13) {
            $('.info').submit();
            displayCustomer();
        }
    })

    // Rest form
    $('#restPage').on('click', function() {
        $('#keyword').val("");
        displayCustomer();
    })

    /* save khi nhan enter */
    $('.enforce').keypress((e) =>{
       if (e.which === 13){
           $('.ss').submit();
           searchBrandName();
       }
    });
    // Show add brand modal
    $('#addBrandInfoModal').on('click', function() {
        resetFormModal($brandInfoForm);
        showModalWithCustomizedTitle($brandInfoModal, "Add New Brand");
        $('#customerid').closest(".form-group").addClass("d-none");
        console.log("Add run...");
    });

    // Show delete brand confirmation modal
    $("#customers").on('click', '.btn-danger', function() {
        console.log('Delete customer');
        $('#deletedBrandName').text($(this).data("name"));
        $('#deleteSubmitBtn').attr("data-id", $(this).data("id"));
        $('#confirmDeleteModal').modal('show');

    });
    // Submit delete brand
    $("#deleteSubmitBtn").on('click' , function() {
        $.ajax({
            url : "/customers/api/delete/" + $(this).attr("data-id"),
            type : 'DELETE',
            dataType : 'json',
            contentType : 'application/json',
            success : function(responseData) {

                $('#confirmDeleteModal').modal('hide');
                showNotification(responseData.responseCode === 100, responseData.responseMsg);

                displayCustomer();

                console.log("Delete success...");
            }
        });
    });

    // Show update brand modal
    $('#customers').on('click', '.btn-primary', function() {

        console.log('Update run...');

        // Get brand info by brand ID
        $.ajax({
            url : "/customers/api/find?id=" + $(this).data("id"),
            type : 'GET',
            dataType : 'json',
            contentType : 'application/json',
            success : function(responseData) {
                if (responseData.responseCode === 100) {
                    const customerInfo = responseData.data;

                    resetFormModal($brandInfoForm); //(Đặt lại hình thức của phương thức trước khi mở phương thức)

                    showModalWithCustomizedTitle($brandInfoModal, "Edit Brand"); //(Thêm tiêu đề cho phương thức đã chọn sau phương thức hiển thị đó)

                    $("#customerid").val(customerInfo.customerid);
                    console.log('ID: ' + customerInfo.customerid);

                    $("#customername").val(customerInfo.customername);
                    console.log('Name: ' + customerInfo.customername);

                    $("#age").val(customerInfo.age);
                    console.log('Age: ' + customerInfo.age);

                    $('#customerId').closest(".form-group").removeClass("d-none");
                    console.log(responseData.data);
                }
            }
        });
    });

    // Submit add and update brand
    $('#saveBrandBtn').on('click', function (event) {

        event.preventDefault();

        const formData = new FormData($brandInfoForm[0]);
        const customerId = formData.get("customerid");
        const isAddAction = customerId === undefined || customerId === "";

        $brandInfoForm.validate({
            rules: {
                customername: {
                    required: true,
                    maxlength: 100
                }
                /*age: {
                    required: isAddAction,
                }*/
            },
            messages: {
                customername: {
                    required: "Please input Customer Name",
                    maxlength: "The Customer Name must be less than 100 characters",
                }
                /*age: {
                    required: "Please input age",
                }*/
            },
            errorElement: "div",
            errorClass: "error-message-invalid"

        });

        if ($brandInfoForm.valid()) {

            console.log('Run add and update....');

            // POST data to server-side by AJAX
            $.ajax({
                url: "/customers/api/" + (isAddAction ? "add" : "update"),
                type: 'POST',
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                timeout: 10000,
                data: formData,
                success: function(responseData) {
                    // Hide modal and show success message when save successfully
                    // Else show error message in modal
                    if (responseData.responseCode === 100) {
                        
                        $brandInfoModal.modal('hide');
                        console.log('Add and update customer success...' + responseData.data)

                        displayCustomer();
                        showNotification(true, responseData.responseMsg);

                    } else {
                        console.log('Add customer Error...' + URLSearchParams)
                        showMsgOnField($brandInfoForm.find('#customername'), responseData.responseMsg);
                    }
                }
            });
        }
    });

});


function resetFormModal($formElement) {

    $formElement[0].reset();
    $formElement.find("input[type*='file']").val("");
    $formElement.validate().destroy();
    $formElement.find(".error-message-invalid").remove();
    // $formElement.find("img").attr('src', '');
}
/**
 * Show notification common
 *
 * @param isSuccess	show notify is success
 * @param message display on notify
 */
function showNotification(isSuccess, message) {

    if (isSuccess) {
        $.notify({
            icon: 'glyphicon glyphicon-ok',
            message: message
        }, {
            type: 'info',
            delay: 3000
        });
    } else {
        $.notify({
            icon: 'glyphicon glyphicon-warning-sign',
            message: message
        }, {
            type: 'danger',
            delay: 6000
        });
    }
}

/**
 * Show message below input field
 *
 * @param $element
 *				element show error message
 * @param isSuccessMsg
 *				true if message is a inform message
 *				false if message is error message
 * @param message
 */
function showMsgOnField($element, message, isSuccessMsg) {

    const className = isSuccessMsg ? "alert-info" : "error-message-invalid";
    $element.find(".form-msg").remove();
    $element.parent().append("<div class='" + className + " form-msg'>" + message + "</div>");

    console.log('Error when add...' + URL);
}

/**
 * Add title for selected modal after that show modal
 * @param $selectedModal
 * @param title
 */
function showModalWithCustomizedTitle($selectedModal, title) {
    $selectedModal.find(".modal-title").text(title);
    $selectedModal.modal('show');
}

function displayCustomer(){
    $.ajax({
        url: '/customers/api',
        type : 'GET',
        dataType : 'json',
        contentType : 'application/json',
        success : function(responseData) {
            if (responseData.responseCode === 100) {
                renderBrandsTable(responseData.data.customerList);
                console.log('Find all: => ' + "http://localhost:8889/customers/api");
            }
        }
    });
}

function renderBrandsTable(customerList) {
    let rowHtml = "";
    $("#customers tbody").empty();
    $.each(customerList, function(key, value) {
        rowHtml = "<tr>"
            +		"<td>" + value.customerid + "</td>"
            +		"<td>" + value.customername + "</td>"
            +		"<td>" + value.age + "</td>"
            +		"<td>"
            +			"<a class='btn btn-primary' data-id='" + value.customerid + "'><i class='far fa-edit'></i></a> | <a class='btn btn-danger' data-name='" + value.customername + "' data-id='" + value.customerid + "'><i class='fas fa-trash-alt'></i></a>"
            +		"</td>"
            +	"</tr>";
        $("#customers tbody").append(rowHtml);
    });
}

/* search */
/**
 * search Brand name
 */
function searchBrandName() {
    const customerName = $('#keyword').val();
    console.log('Name : ' + customerName);
    $.ajax({
        url: "/customers/api/search/" + customerName ,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(responseData) {
            if (responseData.responseCode === 100) {
                renderBrandsTable(responseData.data.customerList);
                /*renderPagination(responseData.data.paginationList);
                if (responseData.data.paginationList.pageNumberList.length < 2) {
                    $('.pagination').addClass("d-none");
                } else {
                    $('.pagination').removeClass("d-none");
                }*/
                renderMessageSearch(responseData.responseMsg);
            }else{
                console.log("===> Error....");
            }
        }
    });
}


function renderMessageSearch(responseMsg) {
    $('#resultSearch p').empty();
    $('#resultSearch p').append(responseMsg);
}
const container = document.getElementById("container");
const signIn = document.getElementById("sign-in");
const signUp = document.getElementById("sign-up");

setTimeout(() => {
  container.classList.add("sign-in");
}, 200);

const toggle = () => {
  container.classList.toggle("sign-in");
  container.classList.toggle("sign-up");
};

signIn.addEventListener("click", toggle);
signUp.addEventListener("click", toggle);

const signUpClick = document.getElementById("btnClick")
signUpClick.addEventListener("click", function (e) {
  e.preventDefault();
  console.log("signUp button clicked..");

  var data = {
    "nickname": $(".text").val(),
    "email": $(".email").val(),
    "password": $(".password").val(),
  }

  $.ajax({
    url: '/signUp',
    processData: false,
    contentType: 'application/json',
    data: JSON.stringify(data),
    type: 'POST',
    dataType: 'json',
    success: function (data) {
      console.log(data);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(textStatus);
    }
  });
});





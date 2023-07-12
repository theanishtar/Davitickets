const loginsec=document.querySelector('.login-section')
const loginlink=document.querySelector('.login-link')
const registerlink=document.querySelector('.register-link')
registerlink.addEventListener('click',()=>{
    loginsec.classList.add('active')
})
loginlink.addEventListener('click',()=>{
    loginsec.classList.remove('active')
})


function showHidePass() {
    var input = document.getElementById("passwordForm");
    if (input.type === "password") {
      input.type = "text";
      document.getElementById("eye").className = "fa-regular fa-eye";
    } else {
      input.type = "password";
      document.getElementById("eye").className = "fa-regular fa-eye-slash";
    }
  }
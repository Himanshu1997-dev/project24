var nameInput = document.getElementById('nameInput');
var getGreetingButton = document.getElementById('getGreetingButton');
var greetingMessage = document.getElementById('greetingMessage');

function handleGetGreeting() {
  var name = nameInput.value.trim();

  fetch('http://localhost:3001/api/greet?name=' + encodeURIComponent(name))
    .then(function(response) {
      if (!response.ok) {
        return response.json().then(function(errorData) {
          throw new Error(errorData.error || 'An error occurred.');
        });
      }
      return response.json();
    })
    .then(function(data) {
      greetingMessage.textContent = data.message;
    })
    .catch(function(error) {
      console.error("Fetch error:", error);
      greetingMessage.textContent = "Error: " + error.message;
    });
}

getGreetingButton.addEventListener('click', handleGetGreeting);
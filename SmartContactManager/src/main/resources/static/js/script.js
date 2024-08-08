console.log("this is consele")

const toggleSideBar = () => {
	if ($('.sidebar').is(":visible")) {
		$(".sidebar").css("display", "none")
		$(".content").css("margin-left", "0%")
	} else {
		$(".sidebar").css("display", "block")
		$(".content").css("margin-left", "20%")
	}
}

const search = () => {
	let query = $('#input-search').val();
	if (query !== '') {
		const url = `http://localhost:8080/search/name/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			}).
			then((data) => {
				let text = `<div class='list-group'>`
				data.forEach((contact) => {
					text += `<a href='/user/${contact.cid}/contactDetail' class='list-group-item list-group-action'>${contact.name}</a>`
				})
				text += `</div>`
				$('.search-result').html(text)
				$('.search-result').show();
			});

	} else {
		$('.search-result').hide();
	}
}
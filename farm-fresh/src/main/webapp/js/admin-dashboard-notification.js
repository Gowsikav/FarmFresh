document.addEventListener('DOMContentLoaded', function() {
    document.addEventListener('click', function(e) {
        const notificationItem = e.target.closest('.notification-item');
        if (notificationItem) {
            e.preventDefault();
            const notificationId = notificationItem.dataset.notificationId; 
            if (notificationId) {
                markNotificationAsRead(notificationId, notificationItem);
            }
        }
    });
});

async function markNotificationAsRead(notificationId, element) {
    try {
        const response = await fetch('/farm-fresh/markNotificationAsRead', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `notificationId=${notificationId}`,
            credentials: 'same-origin'
        });

        if (response.ok) {
            // Remove the notification element
            const notificationElement = document.querySelector(`[data-notification-id="${notificationId}"]`);
            if (notificationElement) {
                notificationElement.remove();
            }

            // Update the badge count
            const badge = document.querySelector('.badge');
            const dropdown = document.querySelector('.dropdown-menu');
            const currentCount = parseInt(badge.textContent) || 0;
            const newCount = currentCount - 1;

            if (newCount > 0) {
                badge.textContent = newCount;
            } else {
                badge.style.display = 'none';
                // Show "No new notifications" message
                dropdown.innerHTML = '<li><a class="dropdown-item text-center text-muted">No new notifications</a></li>';
            }
        } else {
            console.error('Failed to mark notification as read');
        }
    } catch (error) {
        console.error('Failed to mark notification as read:', error);
    }

}

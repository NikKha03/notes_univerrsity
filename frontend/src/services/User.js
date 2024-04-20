import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8080/notes/';

class UserService {
	getPublicContent() {
		return axios.get(API_URL + 'info');
	}

	getUserBoard() {
		return axios.get(API_URL + 'user/board', { headers: authHeader() });
	}

	// getModeratorBoard() {
	// 	return axios.get(API_URL + 'mod', { headers: authHeader() });
	// }

	// getAdminBoard() {
	// 	return axios.get(API_URL + 'admin', { headers: authHeader() });
	// }
}

export default new UserService();

import useAuthStore from '../store/authStore';

export const useAuth = () => {
    const token = useAuthStore((state) => state.token);
    const user = useAuthStore((state) => state.user);
    const roles = useAuthStore((state) => state.roles);
    const login = useAuthStore((state) => state.login);
    const logout = useAuthStore((state) => state.logout);

    const activeRole = roles && roles.length > 0 ? roles[0] : null;

    return {
        token,
        user,
        roles,
        activeRole,
        isAuthenticated: !!token,
        loading: false,
        login,
        logout,
        hasRole: (role) => roles.includes(role),
    };
};

export const AuthProvider = ({ children }) => {
    return children;
};

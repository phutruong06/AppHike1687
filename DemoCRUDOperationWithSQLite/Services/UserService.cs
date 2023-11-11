
using DemoCRUDOperationWithSQLite.Models;
using SQLite;

namespace DemoCRUDOperationWithSQLite.Services
{
    public class UserService : IUserService
    {
        private SQLiteAsyncConnection connection;
        public UserService()
        {
            SetupDatabase();
        }

        public Task<int> AddAsync(User user) => connection.InsertAsync(user);

        public Task<int> DeleteAsync(User user) => connection?.DeleteAsync(user);

        public async Task<List<User>> GetAsync() => await connection.Table<User>().ToListAsync();

        public async Task<User> GetUser(string Name) => await connection.Table<User>().Where(u => u.Name.ToLower().Equals(Name.ToLower())).FirstOrDefaultAsync();
        

        public Task<int> UpdateAsync(User user) => connection.UpdateAsync(user);

        private async void SetupDatabase()
        {
            if(connection is null)
            {
                string dbPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "NetcodeUser.db3");
                connection = new SQLiteAsyncConnection(dbPath);
                await connection.CreateTableAsync<User>();
            }
        }
    }
}

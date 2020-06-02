package Veranstaltungen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class VeraJdbcRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    class VeranstaltungRowMapper implements RowMapper < Veranstaltung > {

        @Override
        public Veranstaltung mapRow(ResultSet rs, int rowNum) throws SQLException {
            Veranstaltung vera = new Veranstaltung();
            vera.setId(rs.getLong("id"));
            vera.setArt(rs.getString("art"));
            vera.setBeschreibung(rs.getString("beschreibung"));
            vera.setVer_name(rs.getString("ver_name"));
            vera.setOrt(rs.getString("ort"));
            vera.setDatum(rs.getString("datum"));
            vera.setRanking(rs.getString("rank"));
            return vera;

        }

    }

    public List < Veranstaltung > findAll() {
        return jdbcTemplate.query("select * from Veranstaltung", new VeranstaltungRowMapper());

    }

    public List < Veranstaltung > findType(String type) {
        return jdbcTemplate.query("SELECT * FROM VERANSTALTUNG WHERE ART =? ", new Object[] {
                type
        },
                new VeranstaltungRowMapper());

    }

    public Veranstaltung findById(long id) {
        return jdbcTemplate.queryForObject("select * from Veranstaltung where id=?", new Object[] {
                    id
                },
                new BeanPropertyRowMapper < Veranstaltung > (Veranstaltung.class));
    }


    public int deleteById(long id) {
        return jdbcTemplate.update("delete from Veranstaltung where id=?", id);
    }

    public int insert(Veranstaltung vera) {

        return jdbcTemplate.update("insert into Veranstaltung (id, ver_name, ort, datum, beschreibung, art, rank) "
                        + "values(?,  ?, ?, ?, ?,?, ?)",

                vera.getId(), vera.getVer_name(), vera.getOrt(), vera.getDatum(), vera.getBeschreibung(),
                vera.getArt(), vera.getRanking());
    }



    public int voteUp(long id, String ranking) {

        int tmp = Integer.parseInt(ranking);
        tmp++;
        String erg = String.valueOf(tmp);
        return jdbcTemplate.update("UPDATE Veranstaltung\n" +
                "        SET RANK = ?\n" +
                "        WHERE ID = ?", erg, id);

    }


    public int voteDown(long id, String ranking) {

        int tmp = Integer.parseInt(ranking);
        tmp--;
        String erg = String.valueOf(tmp);
        return jdbcTemplate.update("UPDATE Veranstaltung\n" +
                "        SET RANK = ?\n" +
                "        WHERE ID = ?", erg, id);

    }






}

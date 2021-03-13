package com.swia.iabuilder.parsers.ttadmiral;

import com.swia.datasets.cards.CardSystem;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.parsers.BaseArmyMarshaller;

import java.util.ArrayList;
import java.util.List;

public class TabletopAdmiralArmyMarshaller extends BaseArmyMarshaller<String, Integer> {

    private static final String BASE_URL = "http://classic.tabletopadmiral.com/imperialassault/nuc";
    private static final String DEPLOYMENT_SEPARATOR = "nuc";
    private static final String COMMAND_SEPARATOR = "ncc";
    private static final int DEPLOYMENT_CODE_LEN = 42;

    public TabletopAdmiralArmyMarshaller() {
        super(new TabletopAdmiralCardParser());
    }

    private List<Integer> getIds(String code) {
        final int n = 3;
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < code.length(); i += n) {
            String c = code.substring(i, i + n);
            Integer id = Integer.parseInt(c);
            ids.add(id);
        }
        return ids;
    }

    private String toCode(List<Integer> ids) {
        StringBuilder code = new StringBuilder();
        for (Integer id : ids) {
            code.append(toCode(id));
        }
        return code.toString();
    }

    private String toCode(Integer id) {
        return String.format("%03d", id);
    }

    @Override
    protected List<Integer> getDeploymentIds(String code) {
        String[] parts = code.split(COMMAND_SEPARATOR);
        if (parts.length < 1) {
            return new ArrayList<>();
        }
        code = parts[0].replace(DEPLOYMENT_SEPARATOR, "");
        return getIds(code);
    }

    @Override
    protected List<Integer> getCommandIds(String code) {
        String[] parts = code.split(COMMAND_SEPARATOR);
        if (parts.length < 2) {
            return new ArrayList<>();
        }
        code = parts[1].replace(COMMAND_SEPARATOR, "");
        return getIds(code);
    }

    @Override
    protected String toDeploymentCode(CardSystem cardSystem, List<Integer> ids) {
        StringBuilder code = new StringBuilder(toCode(ids));
        while (code.length() < DEPLOYMENT_CODE_LEN) {
            code.append('0');
        }
        return code.toString();
    }

    @Override
    public Army deserialize(String code, String name) {
        if (!isValid(code)) {
            return null;
        }
        code = DEPLOYMENT_SEPARATOR + code.replace("https://", "http://")
                   .replace(BASE_URL, "");
        return super.deserialize(code, name);
    }

    @Override
    protected String toCommandCode(CardSystem cardSystem, List<Integer> ids) {
        return toCode(ids);
    }

    @Override
    protected String combine(String deploymentCards, String commandCards) {
        return DEPLOYMENT_SEPARATOR + deploymentCards + COMMAND_SEPARATOR + commandCards;
    }

    @Override
    protected boolean isValid(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        code = code.replace("https://", "http://");
        return code.startsWith(BASE_URL);
    }

    @Override
    public String serialize(Army army) {
        String code = super.serialize(army);
        if (code == null || code.isEmpty()) {
            return null;
        }
        return BASE_URL + code.replace(DEPLOYMENT_SEPARATOR, "");
    }
}
